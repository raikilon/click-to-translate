import { createCompositionRoot } from "../compositionRoot";
import type { BrowserAdapter } from "../platform/BrowserAdapter";
import type {
  ExtensionRequestMessage,
  ExtensionResponseMessage,
  HandleTriggerData,
  MessageError,
  MessageSuccess,
} from "./messageTypes";

function toErrorResponse(error: unknown): MessageError {
  return {
    ok: false,
    error: error instanceof Error ? error.message : "Unknown error",
  };
}

function toSuccessResponse<TData>(data: TData): MessageSuccess<TData> {
  return {
    ok: true,
    data,
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
  ): Promise<HandleTriggerData> {
    const shouldHandleTrigger = await root.useCases.shouldHandleTrigger.execute(
      message.trigger,
    );

    if (!shouldHandleTrigger) {
      return {
        status: "ignored",
        reason: "trigger_mismatch",
        triggerResult: {
          status: "ignored",
          reason: "trigger_mismatch",
        },
      };
    }

    const triggerResult = await root.useCases.handleTrigger.execute(message.trigger, {
      snapshots: message.snapshots,
      fallbackText: "Saved",
    });

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
  ): Promise<ExtensionResponseMessage> {
    switch (message.type) {
      case "GET_SETTINGS": {
        const settings = await root.useCases.getSettings.execute();
        return toSuccessResponse({ settings });
      }

      case "SAVE_SETTINGS": {
        const settings = await root.useCases.saveSettings.execute(message.settings);
        return toSuccessResponse({ settings });
      }

      case "LOGIN": {
        const session = await root.useCases.login.execute();
        return toSuccessResponse({ session });
      }

      case "LOGOUT": {
        await root.useCases.logout.execute();
        return toSuccessResponse({});
      }

      case "GET_LANGUAGES": {
        const result = await root.useCases.loadLanguages.execute();
        return toSuccessResponse({ result });
      }

      case "GET_POPUP_STATE": {
        const [session, settings] = await Promise.all([
          root.authSessionStore.get(),
          root.useCases.getSettings.execute(),
        ]);

        return toSuccessResponse({
          loggedIn: !!session,
          sourceLanguageId: settings.sourceLanguageId,
          targetLanguageId: settings.targetLanguageId,
        });
      }

      case "HANDLE_TRIGGER": {
        return toSuccessResponse(await handleTriggerMessage(message));
      }
    }
  }

  adapter.runtime.addMessageListener((rawMessage) => {
    if (!isExtensionMessage(rawMessage)) {
      return Promise.resolve({
        ok: false,
        error: "Unsupported message payload.",
      } satisfies MessageError);
    }

    return routeMessage(rawMessage).catch((error: unknown) => toErrorResponse(error));
  });
}
