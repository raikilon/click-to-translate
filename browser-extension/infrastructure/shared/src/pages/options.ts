import { DEFAULT_SETTINGS, type Settings } from "@application";
import type { LanguageDto } from "@domain";
import type {
  GetLanguagesData,
  GetSettingsData,
  MessageEnvelope,
  SaveSettingsData,
} from "../background/messageTypes";
import type { RuntimePort } from "../platform/BrowserAdapter";

let currentSettings: Settings = { ...DEFAULT_SETTINGS };
let availableLanguages: LanguageDto[] = [];

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

function normalizeLanguageId(value: string | null | undefined): string {
  return (value ?? "").trim().toLowerCase();
}

function pickLanguageId(languages: LanguageDto[], selectedId: string | null): string {
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

  return match?.id ?? "";
}

function fillLanguageSelect(
  selectId: "sourceLanguageId" | "targetLanguageId",
  placeholder: string,
  selectedId: string | null,
): void {
  const select = byId<HTMLSelectElement>(selectId);
  select.replaceChildren();

  const placeholderOption = document.createElement("option");
  placeholderOption.value = "";
  placeholderOption.textContent = placeholder;
  select.append(placeholderOption);

  for (const language of availableLanguages) {
    const option = document.createElement("option");
    option.value = language.id;
    option.textContent = `${language.name} (${language.code})`;
    select.append(option);
  }

  select.value = pickLanguageId(availableLanguages, selectedId);
}

function fillLanguageSelects(
  languages: LanguageDto[],
  sourceLanguageId: string | null,
  targetLanguageId: string | null,
): void {
  availableLanguages = languages;
  fillLanguageSelect(
    "sourceLanguageId",
    "Select source language",
    sourceLanguageId,
  );
  fillLanguageSelect(
    "targetLanguageId",
    "Select target language",
    targetLanguageId,
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
    sourceLanguageId: byId<HTMLSelectElement>("sourceLanguageId").value || null,
    targetLanguageId: byId<HTMLSelectElement>("targetLanguageId").value || null,
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

export function registerOptions(runtime: RuntimePort): void {
  const sendMessage = createMessageSender(runtime);

  async function loadLanguages(
    preferredSourceLanguageId: string | null,
    preferredTargetLanguageId: string | null,
  ): Promise<void> {
    const response = await sendMessage<GetLanguagesData>({
      type: "GET_LANGUAGES",
    });

    fillLanguageSelects(
      response.result.languages,
      response.result.sourceLanguage?.id ?? preferredSourceLanguageId,
      response.result.targetLanguage?.id ?? preferredTargetLanguageId,
    );
  }

  async function loadSettings(): Promise<void> {
    const response = await sendMessage<GetSettingsData>({
      type: "GET_SETTINGS",
    });

    currentSettings = response.settings;
    fillForm(currentSettings);
    try {
      await loadLanguages(
        currentSettings.sourceLanguageId,
        currentSettings.targetLanguageId,
      );
    } catch (error) {
      fillLanguageSelects([], null, null);
      setStatus(
        error instanceof Error
          ? `Settings loaded, but failed to load languages: ${error.message}`
          : "Settings loaded, but failed to load languages.",
        true,
      );
    }
  }

  async function saveSettings(event: Event): Promise<void> {
    event.preventDefault();

    try {
      const settingsToSave = readFormSettings();
      const response = await sendMessage<SaveSettingsData>({
        type: "SAVE_SETTINGS",
        settings: settingsToSave,
      });

      currentSettings = response.settings;
      fillForm(currentSettings);

      try {
        await loadLanguages(
          currentSettings.sourceLanguageId,
          currentSettings.targetLanguageId,
        );
        setStatus("Settings saved.");
      } catch (error) {
        setStatus(
          error instanceof Error
            ? `Settings saved, but failed to refresh languages: ${error.message}`
            : "Settings saved, but failed to refresh languages.",
          true,
        );
      }
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
