import { createCompositionRoot } from "../compositionRoot";
import type { BrowserAdapter } from "../platform/BrowserAdapter";
import type {
  ErrorMessageResponse,
  ExtensionRequestMessage,
  ExtensionResponseMessage,
  HandleTriggerResponse,
} from "./messageTypes";

function toErrorResponse(error: unknown): ErrorMessageResponse {
  return {
    ok: false,
    error: error instanceof Error ? error.message : "Unknown error",
  };
}

function isExtensionMessage(value: unknown): value is ExtensionRequestMessage {
  return (
    typeof value === "object" &&
    value !== null &&
    "type" in value &&
    typeof (value as { type: unknown }).type === "string"
  );
}

export function registerBackground(adapter: BrowserAdapter): void {
  const root = createCompositionRoot(adapter);

  async function handleTriggerMessage(
    message: Extract<ExtensionRequestMessage, { type: "HANDLE_TRIGGER" }>,
  ): Promise<HandleTriggerResponse> {
    const triggerResult = await root.useCases.handleTrigger.execute(message.trigger, {
      snapshots: message.snapshots,
      fallbackText: "Saved",
    });

    if (
      triggerResult.status === "rendered" ||
      (triggerResult.status === "no_translation" &&
        !!triggerResult.instruction &&
        !!triggerResult.renderPayload)
    ) {
      return {
        status: "ok",
        instruction: triggerResult.instruction,
        renderPayload: triggerResult.renderPayload,
        triggerResult,
      };
    }

    return {
      status: triggerResult.status,
      reason: triggerResult.reason,
      instruction: triggerResult.instruction,
      renderPayload: triggerResult.renderPayload,
      triggerResult,
    };
  }

  async function routeMessage(
    message: ExtensionRequestMessage,
  ): Promise<ExtensionResponseMessage | HandleTriggerResponse> {
    switch (message.type) {
      case "GET_SETTINGS": {
        const settings = await root.useCases.getSettings.execute();
        return { ok: true, settings };
      }

      case "SAVE_SETTINGS": {
        const settings = await root.useCases.saveSettings.execute(message.settings);
        return { ok: true, settings };
      }

      case "LOGIN": {
        const session = await root.useCases.login.execute();
        return { ok: true, session };
      }

      case "LOGOUT": {
        await root.useCases.logout.execute();
        return { ok: true };
      }

      case "GET_LANGUAGES": {
        const result = await root.useCases.loadLanguages.execute();
        return { ok: true, result };
      }

      case "HANDLE_TRIGGER": {
        return handleTriggerMessage(message);
      }
    }
  }

  adapter.runtime.addMessageListener((rawMessage) => {
    if (!isExtensionMessage(rawMessage)) {
      return Promise.resolve({
        ok: false,
        error: "Unsupported message payload.",
      } satisfies ErrorMessageResponse);
    }

    return routeMessage(rawMessage).catch((error: unknown) => toErrorResponse(error));
  });
}
