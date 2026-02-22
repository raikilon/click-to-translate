import { DEFAULT_SETTINGS, type MouseButton, type Renderer } from "@application";
import type { RenderPayload } from "@application";
import type { SettingsModifiers } from "@application";
import type { Snapshots, Trigger } from "@domain";
import type { DisplayInstruction } from "@domain";
import type {
  HandleTriggerData,
  HandleTriggerResponse,
  MessageEnvelope,
} from "../background/messageTypes";
import type { RuntimePort, StoragePort } from "../platform/BrowserAdapter";

const SETTINGS_KEY = "settings";
const TRIGGER_SETTINGS_CACHE_TTL_MS = 3_000;

interface TriggerSettings {
  mouseButton: MouseButton;
  modifiers: SettingsModifiers;
}

export interface ContentScriptDependencies {
  runtime: RuntimePort;
  storage: StoragePort;
  renderer: Renderer;
  collectSnapshots(trigger: Trigger): Promise<Snapshots>;
  nowMs(): number;
}

function toMouseButton(button: number): "left" | "middle" | "right" {
  if (button === 1) {
    return "middle";
  }

  if (button === 2) {
    return "right";
  }

  return "left";
}

type RenderableHandleTriggerData = HandleTriggerData & {
  instruction: DisplayInstruction;
  renderPayload: RenderPayload;
};

function shouldRender(
  payload: HandleTriggerData,
): payload is RenderableHandleTriggerData {
  if (!payload.instruction || !payload.renderPayload) {
    return false;
  }

  return payload.status === "rendered" || payload.status === "no_translation";
}

function isMouseButton(value: unknown): value is MouseButton {
  return value === "left" || value === "middle" || value === "right";
}

function normalizeTriggerSettings(candidate: unknown): TriggerSettings {
  if (!candidate || typeof candidate !== "object") {
    return {
      mouseButton: DEFAULT_SETTINGS.mouseButton,
      modifiers: { ...DEFAULT_SETTINGS.modifiers },
    };
  }

  const value = candidate as {
    mouseButton?: unknown;
    modifiers?: Partial<SettingsModifiers>;
  };

  return {
    mouseButton: isMouseButton(value.mouseButton)
      ? value.mouseButton
      : DEFAULT_SETTINGS.mouseButton,
    modifiers: {
      alt:
        typeof value.modifiers?.alt === "boolean"
          ? value.modifiers.alt
          : DEFAULT_SETTINGS.modifiers.alt,
      ctrl:
        typeof value.modifiers?.ctrl === "boolean"
          ? value.modifiers.ctrl
          : DEFAULT_SETTINGS.modifiers.ctrl,
      shift:
        typeof value.modifiers?.shift === "boolean"
          ? value.modifiers.shift
          : DEFAULT_SETTINGS.modifiers.shift,
      meta:
        typeof value.modifiers?.meta === "boolean"
          ? value.modifiers.meta
          : DEFAULT_SETTINGS.modifiers.meta,
    },
  };
}

function triggerMatchesSettings(
  trigger: Trigger,
  settings: TriggerSettings,
): boolean {
  return (
    trigger.mouse.button === settings.mouseButton &&
    trigger.modifiers.alt === settings.modifiers.alt &&
    trigger.modifiers.ctrl === settings.modifiers.ctrl &&
    trigger.modifiers.shift === settings.modifiers.shift &&
    trigger.modifiers.meta === settings.modifiers.meta
  );
}

export function registerContentScript(dependencies: ContentScriptDependencies): void {
  let cachedTriggerSettings: TriggerSettings | null = null;
  let cacheUpdatedAtMs = 0;
  let inFlightLoad: Promise<TriggerSettings> | null = null;

  function toTrigger(event: MouseEvent): Trigger {
    const selectedText = window.getSelection()?.toString().trim();

    return {
      url: window.location.href,
      mouse: {
        button: toMouseButton(event.button),
        x: event.clientX,
        y: event.clientY,
      },
      modifiers: {
        alt: event.altKey,
        ctrl: event.ctrlKey,
        shift: event.shiftKey,
        meta: event.metaKey,
      },
      selectedText: selectedText || undefined,
      occurredAtMs: dependencies.nowMs(),
    };
  }

  function isCacheFresh(nowMs: number): boolean {
    return (
      !!cachedTriggerSettings &&
      nowMs - cacheUpdatedAtMs <= TRIGGER_SETTINGS_CACHE_TTL_MS
    );
  }

  async function loadTriggerSettingsFromStorage(): Promise<TriggerSettings> {
    const rawSettings = await dependencies.storage.get<unknown>(SETTINGS_KEY);
    return normalizeTriggerSettings(rawSettings);
  }

  async function getTriggerSettings(): Promise<TriggerSettings> {
    const nowMs = dependencies.nowMs();
    if (isCacheFresh(nowMs) && cachedTriggerSettings) {
      return cachedTriggerSettings;
    }

    if (!inFlightLoad) {
      inFlightLoad = loadTriggerSettingsFromStorage()
        .then((settings) => {
          cachedTriggerSettings = settings;
          cacheUpdatedAtMs = dependencies.nowMs();
          return settings;
        })
        .finally(() => {
          inFlightLoad = null;
        });
    }

    return inFlightLoad;
  }

  async function sendHandleTrigger(
    trigger: Trigger,
  ): Promise<MessageEnvelope<HandleTriggerData>> {
    const snapshots = await dependencies.collectSnapshots(trigger);
    return dependencies.runtime.sendMessage<HandleTriggerResponse>(
      {
        type: "HANDLE_TRIGGER",
        trigger,
        snapshots,
      },
    );
  }

  void getTriggerSettings().catch(() => {
    // Settings fallback to defaults if initial storage read fails.
  });

  window.addEventListener("click", (event: MouseEvent) => {
    const trigger = toTrigger(event);

    void getTriggerSettings()
      .then((settings) => {
        if (!triggerMatchesSettings(trigger, settings)) {
          return null;
        }

        return sendHandleTrigger(trigger);
      })
      .then(async (response) => {
        if (!response) {
          return;
        }

        if (!response.ok) {
          return;
        }

        if (shouldRender(response.data)) {
          await dependencies.renderer.render(
            response.data.instruction,
            response.data.renderPayload,
          );
        }
      })
      .catch(() => {
        // Ignore transient messaging/runtime errors in content context.
      });
  });
}
