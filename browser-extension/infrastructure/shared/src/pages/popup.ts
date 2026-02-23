import type { Settings } from "@application";
import type { LanguageDto } from "@domain";
import type {
  GetSettingsData,
  GetLanguagesData,
  GetPopupStateData,
  LoginData,
  MessageEnvelope,
  SaveSettingsData,
} from "../background/messageTypes";
import type { RuntimePort } from "../platform/BrowserAdapter";

interface PopupDependencies {
  runtime: RuntimePort;
}

function byId<T extends HTMLElement>(id: string): T {
  const element = document.getElementById(id);
  if (!element) {
    throw new Error(`Missing popup element: ${id}`);
  }

  return element as T;
}

function setStatus(message: string, isError = false): void {
  const status = byId<HTMLDivElement>("status");
  status.textContent = message;
  status.dataset.kind = isError ? "error" : "ok";
}

function normalizeLanguageId(value: string | null | undefined): string {
  return (value ?? "").trim().toLowerCase();
}

function pickLanguageCode(
  languages: LanguageDto[],
  selectedId: string | null,
): string {
  const normalizedSelectedId = normalizeLanguageId(selectedId);
  if (!normalizedSelectedId) {
    return "";
  }

  const match = languages.find((language) => {
    const normalizedId = normalizeLanguageId(language.id);
    const normalizedCode = normalizeLanguageId(language.code);
    return (
      normalizedId === normalizedSelectedId || normalizedCode === normalizedSelectedId
    );
  });

  return match?.code ?? "";
}

function fillLanguageSelect(
  select: HTMLSelectElement,
  placeholder: string,
  languages: LanguageDto[],
  selectedId: string | null,
): void {
  select.replaceChildren();

  const placeholderOption = document.createElement("option");
  placeholderOption.value = "";
  placeholderOption.textContent = placeholder;
  select.append(placeholderOption);

  for (const language of languages) {
    const option = document.createElement("option");
    option.value = language.code;
    option.textContent = `${language.name} (${language.code})`;
    select.append(option);
  }

  select.value = pickLanguageCode(languages, selectedId);
  select.disabled = languages.length === 0;
}

function createMessageSender(runtime: RuntimePort) {
  return async function sendMessage<TData>(message: unknown): Promise<TData> {
    const response = await runtime.sendMessage<MessageEnvelope<TData>>(message);
    if (!response) {
      throw new Error("No response from background.");
    }

    if (!response.ok) {
      throw new Error(response.error);
    }

    return response.data;
  };
}

export function registerPopup(dependencies: PopupDependencies): void {
  const sendMessage = createMessageSender(dependencies.runtime);
  const sessionLabel = byId<HTMLDivElement>("sessionState");
  const loginButton = byId<HTMLButtonElement>("loginButton");
  const logoutButton = byId<HTMLButtonElement>("logoutButton");
  const languageSection = byId<HTMLElement>("languageSection");
  const sourceSelect = byId<HTMLSelectElement>("sourceLanguageId");
  const targetSelect = byId<HTMLSelectElement>("targetLanguageId");
  let currentSettings: Settings | null = null;
  let availableLanguages: LanguageDto[] = [];
  let suppressAutoSave = false;

  function applyAuthState(loggedIn: boolean): void {
    sessionLabel.textContent = loggedIn ? "Session: Logged in" : "Session: Logged out";
    loginButton.hidden = loggedIn;
    logoutButton.hidden = !loggedIn;
    languageSection.hidden = !loggedIn;
  }

  function applyLanguageSelections(
    sourceLanguageId: string | null,
    targetLanguageId: string | null,
  ): void {
    suppressAutoSave = true;
    fillLanguageSelect(
      sourceSelect,
      "Select source language",
      availableLanguages,
      sourceLanguageId,
    );
    fillLanguageSelect(
      targetSelect,
      "Select target language",
      availableLanguages,
      targetLanguageId,
    );
    suppressAutoSave = false;
  }

  async function loadSettings(): Promise<void> {
    const response = await sendMessage<GetSettingsData>({
      type: "GET_SETTINGS",
    });
    currentSettings = response.settings;
  }

  async function loadLanguages(): Promise<void> {
    const response = await sendMessage<GetLanguagesData>({
      type: "GET_LANGUAGES",
    });

    availableLanguages = response.result.languages;
    applyLanguageSelections(
      response.result.sourceLanguage?.code ?? currentSettings?.sourceLanguageId ?? null,
      response.result.targetLanguage?.code ?? currentSettings?.targetLanguageId ?? null,
    );
  }

  async function refreshPopupState(): Promise<void> {
    const popupState = await sendMessage<GetPopupStateData>({
      type: "GET_POPUP_STATE",
    });

    applyAuthState(popupState.loggedIn);
    if (!popupState.loggedIn) {
      availableLanguages = [];
      applyLanguageSelections(null, null);
      return;
    }

    await loadSettings();
    await loadLanguages();
  }

  async function onLogin(): Promise<void> {
    const response = await sendMessage<LoginData>({ type: "LOGIN" });
    if (!response.session) {
      throw new Error("Login did not return an active session.");
    }

    setStatus("Login successful.");
    await refreshPopupState();
  }

  async function onLogout(): Promise<void> {
    await sendMessage({ type: "LOGOUT" });
    setStatus("Logged out.");
    await refreshPopupState();
  }

  async function onLanguageSelectionChanged(): Promise<void> {
    if (suppressAutoSave || !currentSettings) {
      return;
    }

    const response = await sendMessage<SaveSettingsData>({
      type: "SAVE_SETTINGS",
      settings: {
        ...currentSettings,
        sourceLanguageId: sourceSelect.value.trim() || null,
        targetLanguageId: targetSelect.value.trim() || null,
      },
    });

    currentSettings = response.settings;
    setStatus("Language preferences saved.");
  }

  loginButton.addEventListener("click", () => {
    void onLogin().catch((error: unknown) => {
      setStatus(error instanceof Error ? error.message : "Login failed.", true);
    });
  });

  logoutButton.addEventListener("click", () => {
    void onLogout().catch((error: unknown) => {
      setStatus(error instanceof Error ? error.message : "Logout failed.", true);
    });
  });

  sourceSelect.addEventListener("change", () => {
    void onLanguageSelectionChanged().catch((error: unknown) => {
      setStatus(
        error instanceof Error ? error.message : "Failed to save source language.",
        true,
      );
    });
  });

  targetSelect.addEventListener("change", () => {
    void onLanguageSelectionChanged().catch((error: unknown) => {
      setStatus(
        error instanceof Error ? error.message : "Failed to save target language.",
        true,
      );
    });
  });

  void refreshPopupState().catch((error: unknown) => {
    setStatus(error instanceof Error ? error.message : "Failed to load popup state.", true);
  });
}
