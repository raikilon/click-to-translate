import type { Trigger } from "@domain";
import type {
  ErrorMessageResponse,
  HandleTriggerResponse,
} from "../background/messageTypes";
import { collectSnapshots } from "./probes/probeResolver";
import { ContentRendererBridge } from "./render/rendererBridge";

const renderer = new ContentRendererBridge();

function toMouseButton(button: number): "left" | "middle" | "right" {
  if (button === 1) {
    return "middle";
  }

  if (button === 2) {
    return "right";
  }

  return "left";
}

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
    occurredAtMs: Date.now(),
  };
}

async function sendHandleTrigger(
  trigger: Trigger,
): Promise<HandleTriggerResponse | ErrorMessageResponse> {
  const snapshots = await collectSnapshots(trigger);
  const response = (await browser.runtime.sendMessage({
    type: "HANDLE_TRIGGER",
    trigger,
    snapshots,
  })) as HandleTriggerResponse | ErrorMessageResponse | undefined;

  if (!response) {
    throw new Error("Background did not return a response.");
  }

  return response;
}

function isErrorResponse(
  response: HandleTriggerResponse | ErrorMessageResponse,
): response is ErrorMessageResponse {
  return "ok" in response && response.ok === false;
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
        await renderer.render(response.instruction, response.renderPayload);
      }
    })
    .catch(() => {
      // Ignore transient messaging/runtime errors in content context.
    });
});
