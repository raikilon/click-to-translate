import type { Renderer } from "@application";
import type { RenderPayload } from "@application";
import type { Snapshots, Trigger } from "@domain";
import type { DisplayInstruction } from "@domain";
import type {
  HandleTriggerData,
  HandleTriggerResponse,
  MessageEnvelope,
} from "../background/messageTypes";
import type { RuntimePort } from "../platform/BrowserAdapter";

export interface ContentScriptDependencies {
  runtime: RuntimePort;
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

export function registerContentScript(dependencies: ContentScriptDependencies): void {
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

  window.addEventListener("click", (event: MouseEvent) => {
    const trigger = toTrigger(event);

    void sendHandleTrigger(trigger)
      .then(async (response) => {
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
