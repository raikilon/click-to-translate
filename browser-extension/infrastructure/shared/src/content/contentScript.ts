import type { Renderer } from "@application";
import type { Snapshots, Trigger } from "@domain";
import type {
  ErrorMessageResponse,
  HandleTriggerResponse,
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

function isErrorResponse(
  response: HandleTriggerResponse | ErrorMessageResponse,
): response is ErrorMessageResponse {
  return "ok" in response && response.ok === false;
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
  ): Promise<HandleTriggerResponse | ErrorMessageResponse> {
    const snapshots = await dependencies.collectSnapshots(trigger);
    return dependencies.runtime.sendMessage<HandleTriggerResponse | ErrorMessageResponse>(
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
        if (isErrorResponse(response)) {
          return;
        }

        if (
          response.status === "ok" &&
          response.instruction &&
          response.renderPayload
        ) {
          await dependencies.renderer.render(response.instruction, response.renderPayload);
        }
      })
      .catch(() => {
        // Ignore transient messaging/runtime errors in content context.
      });
  });
}
