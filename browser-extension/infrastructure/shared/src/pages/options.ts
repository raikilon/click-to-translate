import { DEFAULT_SETTINGS, type Settings } from "@application";
import type {
  ErrorMessageResponse,
  GetSettingsResponse,
  SaveSettingsResponse,
} from "../background/messageTypes";
import type { RuntimePort } from "../platform/BrowserAdapter";

let currentSettings: Settings = { ...DEFAULT_SETTINGS };

function byId<T extends HTMLElement>(id: string): T {
  const element = document.getElementById(id);
  if (!element) {
    throw new Error(`Missing options element: ${id}`);
  }

  return element as T;
}

function setStatus(message: string, isError = false): void {
  const status = byId<HTMLDivElement>("status");
  status.textContent = message;
  status.dataset.kind = isError ? "error" : "ok";
}

function isErrorResponse<TResponse>(
  response: TResponse | ErrorMessageResponse,
): response is ErrorMessageResponse {
  return (
    response &&
    typeof response === "object" &&
    "ok" in response &&
    (response as ErrorMessageResponse).ok === false
  );
}

function fillForm(settings: Settings): void {
  byId<HTMLInputElement>("apiBaseUrl").value = settings.apiBaseUrl;
  byId<HTMLInputElement>("languagesPath").value = settings.languagesPath;
  byId<HTMLInputElement>("segmentsPath").value = settings.segmentsPath;
  byId<HTMLInputElement>("authAuthorizeUrl").value = settings.authAuthorizeUrl;
  byId<HTMLInputElement>("authTokenUrl").value = settings.authTokenUrl;
  byId<HTMLInputElement>("oauthClientId").value = settings.oauthClientId;
  byId<HTMLInputElement>("scopes").value = settings.scopes.join(" ");
  byId<HTMLInputElement>("sourceLanguageId").value = settings.sourceLanguageId ?? "";
  byId<HTMLInputElement>("targetLanguageId").value = settings.targetLanguageId ?? "";
  byId<HTMLSelectElement>("mouseButton").value = settings.mouseButton;
  byId<HTMLInputElement>("modAlt").checked = settings.modifiers.alt;
  byId<HTMLInputElement>("modCtrl").checked = settings.modifiers.ctrl;
  byId<HTMLInputElement>("modShift").checked = settings.modifiers.shift;
  byId<HTMLInputElement>("modMeta").checked = settings.modifiers.meta;
  byId<HTMLInputElement>("showTooltip").checked = settings.showTooltip;
}

function readFormSettings(): Settings {
  const scopes = byId<HTMLInputElement>("scopes")
    .value.split(/[\s,]+/)
    .map((scope) => scope.trim())
    .filter(Boolean);

  return {
    ...currentSettings,
    apiBaseUrl: byId<HTMLInputElement>("apiBaseUrl").value.trim(),
    languagesPath: byId<HTMLInputElement>("languagesPath").value.trim(),
    segmentsPath: byId<HTMLInputElement>("segmentsPath").value.trim(),
    authAuthorizeUrl: byId<HTMLInputElement>("authAuthorizeUrl").value.trim(),
    authTokenUrl: byId<HTMLInputElement>("authTokenUrl").value.trim(),
    oauthClientId: byId<HTMLInputElement>("oauthClientId").value.trim(),
    scopes,
    sourceLanguageId: byId<HTMLInputElement>("sourceLanguageId").value.trim() || null,
    targetLanguageId: byId<HTMLInputElement>("targetLanguageId").value.trim() || null,
    mouseButton: byId<HTMLSelectElement>("mouseButton").value as Settings["mouseButton"],
    modifiers: {
      alt: byId<HTMLInputElement>("modAlt").checked,
      ctrl: byId<HTMLInputElement>("modCtrl").checked,
      shift: byId<HTMLInputElement>("modShift").checked,
      meta: byId<HTMLInputElement>("modMeta").checked,
    },
    showTooltip: byId<HTMLInputElement>("showTooltip").checked,
  };
}

function createMessageSender(runtime: RuntimePort) {
  return async function sendMessage<TResponse>(message: unknown): Promise<TResponse> {
    const response = await runtime.sendMessage<TResponse | ErrorMessageResponse>(message);
    if (!response) {
      throw new Error("No response from background.");
    }

    if (isErrorResponse(response)) {
      throw new Error(response.error);
    }

    return response as TResponse;
  };
}

export function registerOptions(runtime: RuntimePort): void {
  const sendMessage = createMessageSender(runtime);

  async function loadSettings(): Promise<void> {
    const response = await sendMessage<GetSettingsResponse>({
      type: "GET_SETTINGS",
    });

    currentSettings = response.settings;
    fillForm(currentSettings);
  }

  async function saveSettings(event: Event): Promise<void> {
    event.preventDefault();

    try {
      const settingsToSave = readFormSettings();
      const response = await sendMessage<SaveSettingsResponse>({
        type: "SAVE_SETTINGS",
        settings: settingsToSave,
      });

      currentSettings = response.settings;
      setStatus("Settings saved.");
    } catch (error) {
      setStatus(error instanceof Error ? error.message : "Save failed.", true);
    }
  }

  const form = byId<HTMLFormElement>("optionsForm");
  form.addEventListener("submit", (event) => {
    void saveSettings(event);
  });

  void loadSettings().catch((error: unknown) => {
    setStatus(error instanceof Error ? error.message : "Failed to load settings.", true);
  });
}
