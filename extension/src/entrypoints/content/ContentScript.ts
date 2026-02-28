import type { TranslateClickResult } from "@application";
import type { Anchor } from "@domain";
import type { BackgroundClient } from "../shared/messaging/client";
import type { PointCapture } from "./capture/types";
import { ContentTriggerPrefs } from "./ContentTriggerPrefs";
import type { PageRenderer } from "./render/PageRenderer";

export interface ContentScriptDependencies {
  client: BackgroundClient;
  pointCapture: PointCapture;
  renderer: PageRenderer;
}

type RenderableTranslateClickResult = TranslateClickResult & {
  status: "rendered";
  translatedText: string;
  anchor: Anchor;
};

export class ContentScript {
  private readonly triggerPrefs: ContentTriggerPrefs;

  constructor(private readonly dependencies: ContentScriptDependencies) {
    this.triggerPrefs = new ContentTriggerPrefs();
  }

  register(): void {
    this.triggerPrefs.initialize();
    window.addEventListener("click", this.onWindowClick.bind(this));
  }

  private onWindowClick(event: MouseEvent): void {
    if (!this.triggerPrefs.matchesEvent(event)) {
      return;
    }

    const capturedClick = this.dependencies.pointCapture.capture({
      x: event.clientX,
      y: event.clientY,
    });

    if (!capturedClick) {
      return;
    }

    void this.dependencies.client
      .translateAtPoint(capturedClick)
      .then(this.onTranslateAtPointResolved.bind(this))
      .catch(this.ignoreTransientTranslateAtPointFailure);
  }

  private async onTranslateAtPointResolved(
    result: TranslateClickResult,
  ): Promise<void> {
    if (!this.isRenderableTranslateResult(result)) {
      return;
    }

    await this.dependencies.renderer.render(
      result.anchor,
      result.translatedText,
    );
  }
  private ignoreTransientTranslateAtPointFailure(_error: unknown): void {}

  private isRenderableTranslateResult(
    result: TranslateClickResult,
  ): result is RenderableTranslateClickResult {
    return !!(
      result.status === "rendered" &&
      result.translatedText &&
      result.anchor
    );
  }
}
