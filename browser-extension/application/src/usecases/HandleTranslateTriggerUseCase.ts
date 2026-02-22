import {
  createDefaultStrategies,
  StrategyResolver,
  type CaptureResult,
  type DisplayInstruction,
  type SegmentBundleDto,
  type Snapshots,
  type SourceMetadataDto,
  type Trigger,
} from "@domain";
import type { ApiClient } from "../contracts/ApiClient";
import type { Clock } from "../contracts/Clock";
import type { PageInfo, PageProbe } from "../contracts/PageProbe";
import type { RenderPayload, Renderer } from "../contracts/Renderer";
import type { SettingsStore } from "../contracts/SettingsStore";
import type { PostSegmentResponse } from "../model/ApiModels";
import type { Settings } from "../model/Settings";
import type { EnsureAuthSessionUseCase } from "./EnsureAuthSessionUseCase";
import { languageFromCode } from "./LanguageUtils";

export type HandleTranslateTriggerStatus =
  | "ignored"
  | "missing_languages"
  | "no_capture"
  | "no_translation"
  | "rendered";

export interface HandleTranslateTriggerResult {
  status: HandleTranslateTriggerStatus;
  reason?: string;
  capture?: CaptureResult;
  response?: PostSegmentResponse;
  instruction?: DisplayInstruction;
  renderPayload?: RenderPayload;
}

export interface HandleTranslateTriggerExecuteOptions {
  snapshots?: Snapshots;
  render?: boolean;
  fallbackText?: string;
}

export class HandleTranslateTriggerUseCase {
  private readonly strategyResolver: StrategyResolver;

  constructor(
    private readonly settingsStore: SettingsStore,
    private readonly pageProbe: PageProbe,
    private readonly apiClient: ApiClient,
    private readonly renderer: Renderer,
    private readonly clock: Clock,
    private readonly ensureAuthSession?: EnsureAuthSessionUseCase,
    strategyResolver?: StrategyResolver,
  ) {
    this.strategyResolver =
      strategyResolver ?? new StrategyResolver(createDefaultStrategies());
  }

  async execute(
    trigger: Trigger,
    options: HandleTranslateTriggerExecuteOptions = {},
  ): Promise<HandleTranslateTriggerResult> {
    const settings = await this.settingsStore.get();
    if (!triggerMatchesSettings(trigger, settings)) {
      return {
        status: "ignored",
        reason: "trigger_mismatch",
      };
    }

    const snapshots = options.snapshots ?? (await this.captureSnapshots(trigger));
    const pageInfo = snapshots.pageInfo ?? { url: trigger.url };

    const strategy = this.strategyResolver.resolve(
      pageInfo.url || trigger.url,
      settings.siteOverrides,
    );
    const capture = strategy.computeCapture(trigger, snapshots);
    if (!capture) {
      return {
        status: "no_capture",
      };
    }

    const sourceLanguage = languageFromCode(settings.sourceLanguageId ?? "");
    const targetLanguage = languageFromCode(settings.targetLanguageId ?? "");

    if (!sourceLanguage || !targetLanguage) {
      return {
        status: "missing_languages",
      };
    }

    const bundle: SegmentBundleDto = {
      word: capture.word,
      sentence: capture.sentence,
      sourceLanguage,
      targetLanguage,
      source: capture.source,
      sourceMetadata: buildSourceMetadata(
        capture.sourceMetadata,
        pageInfo,
        trigger.url,
      ),
      occurredAt: new Date(this.clock.nowMs()).toISOString(),
    };

    const accessToken = await this.resolveAccessToken();
    const response = await this.apiClient.postSegment(accessToken, bundle);
    const translatedText = pickTranslatedText(response);
    const instruction: DisplayInstruction = settings.showTooltip
      ? strategy.computeDisplay(capture, trigger, snapshots)
      : {
          mode: "POPUP_ONLY",
          anchor: capture.anchor,
        };
    const renderPayload = buildRenderPayload(
      capture,
      response,
      translatedText,
      options.fallbackText,
    );
    const shouldRender = options.render ?? true;
    if (shouldRender) {
      await this.renderer.render(instruction, renderPayload);
    }

    if (!translatedText) {
      return {
        status: "no_translation",
        capture,
        response,
        instruction,
        renderPayload,
      };
    }

    return {
      status: "rendered",
      capture,
      response,
      instruction,
      renderPayload,
    };
  }

  private async captureSnapshots(trigger: Trigger): Promise<Snapshots> {
    const [pageInfo, selection, textAtPoint, subtitle] = await Promise.all([
      this.pageProbe.getPageInfo(),
      this.pageProbe.getSelectionSnapshot(trigger),
      this.pageProbe.getTextAtPoint(trigger),
      this.pageProbe.getSubtitleSnapshot(trigger),
    ]);

    return {
      pageInfo,
      selection,
      textAtPoint,
      subtitle,
    };
  }

  private async resolveAccessToken(): Promise<string> {
    if (!this.ensureAuthSession) {
      return "";
    }

    const session = await this.ensureAuthSession.execute({ interactive: false });
    return session?.accessToken ?? "";
  }
}

function triggerMatchesSettings(trigger: Trigger, settings: Settings): boolean {
  return (
    trigger.mouse.button === settings.mouseButton &&
    trigger.modifiers.alt === settings.modifiers.alt &&
    trigger.modifiers.ctrl === settings.modifiers.ctrl &&
    trigger.modifiers.shift === settings.modifiers.shift &&
    trigger.modifiers.meta === settings.modifiers.meta
  );
}

function parseUrl(
  value: string,
): {
  hostname?: string;
  path?: string;
} {
  try {
    const parsed = new URL(value);
    return {
      hostname: parsed.hostname || undefined,
      path: parsed.pathname || undefined,
    };
  } catch {
    return {};
  }
}

function buildSourceMetadata(
  metadataFromCapture: SourceMetadataDto | undefined,
  pageInfo: PageInfo,
  fallbackUrl: string,
): SourceMetadataDto | undefined {
  const url = metadataFromCapture?.url ?? pageInfo.url ?? fallbackUrl;
  if (!url) {
    return metadataFromCapture;
  }

  const parsedUrl = parseUrl(url);

  return {
    url,
    title: metadataFromCapture?.title ?? pageInfo.title,
    hostname:
      metadataFromCapture?.hostname ?? pageInfo.hostname ?? parsedUrl.hostname,
    path: metadataFromCapture?.path ?? parsedUrl.path,
    elementHint: metadataFromCapture?.elementHint,
  };
}

function pickTranslatedText(response: PostSegmentResponse): string | null {
  const translatedSentence = response.translatedSentence?.trim();
  if (translatedSentence) {
    return translatedSentence;
  }

  const translatedWord = response.translatedWord?.trim();
  if (translatedWord) {
    return translatedWord;
  }

  const translatedText = response.translationText?.trim();
  if (translatedText) {
    return translatedText;
  }

  return null;
}

function buildRenderPayload(
  capture: CaptureResult,
  response: PostSegmentResponse,
  translatedText: string | null,
  fallbackText?: string,
): RenderPayload {
  return {
    text: translatedText ?? buildPlaceholderText(response, fallbackText),
    debug: {
      word: capture.word,
      sentence: capture.sentence,
    },
  };
}

function buildPlaceholderText(
  response: PostSegmentResponse,
  fallbackText?: string,
): string {
  const normalizedFallback = fallbackText?.trim() ? fallbackText.trim() : "Saved";
  const segmentId = response.segmentId?.trim();
  return segmentId ? `${normalizedFallback} (#${segmentId})` : normalizedFallback;
}
