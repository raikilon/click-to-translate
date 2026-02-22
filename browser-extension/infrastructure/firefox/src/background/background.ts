import { createCompositionRoot } from "../compositionRoot";
import type {
  ErrorMessageResponse,
  ExtensionRequestMessage,
  ExtensionResponseMessage,
  HandleTriggerResponse,
} from "./messageTypes";

const root = createCompositionRoot();

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

async function handleTriggerMessage(
  message: Extract<ExtensionRequestMessage, { type: "HANDLE_TRIGGER" }>,
  sender: browser.runtime.MessageSender,
): Promise<HandleTriggerResponse> {
  const senderTabId = sender.tab?.id;
  if (typeof senderTabId !== "number") {
    // Keep MVP behavior simple and reply directly even when tab id is absent.
  }

  const triggerResult = await root.useCases.handleTrigger.execute(message.trigger, {
    snapshots: message.snapshots,
    render: false,
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
  sender: browser.runtime.MessageSender,
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
      return handleTriggerMessage(message, sender);
    }
  }
}

browser.runtime.onMessage.addListener((rawMessage, sender) => {
  if (!isExtensionMessage(rawMessage)) {
    return Promise.resolve({
      ok: false,
      error: "Unsupported message payload.",
    } satisfies ErrorMessageResponse);
  }

  return routeMessage(rawMessage, sender).catch((error: unknown) =>
    toErrorResponse(error),
  );
});
