import type { SettingsStore } from "@application";
import { DEFAULT_SETTINGS, type Settings } from "@application";
import type { StoragePort } from "../platform/BrowserAdapter";

const SETTINGS_KEY = "settings";

function normalizeSettings(candidate: unknown): Settings {
  if (!candidate || typeof candidate !== "object") {
    return { ...DEFAULT_SETTINGS };
  }

  const value = candidate as Partial<Settings>;

  return {
    ...DEFAULT_SETTINGS,
    ...value,
    scopes: Array.isArray(value.scopes)
      ? value.scopes.filter((scope): scope is string => typeof scope === "string")
      : DEFAULT_SETTINGS.scopes,
    modifiers: {
      ...DEFAULT_SETTINGS.modifiers,
      ...value.modifiers,
    },
    siteOverrides: Array.isArray(value.siteOverrides)
      ? value.siteOverrides
      : DEFAULT_SETTINGS.siteOverrides,
  };
}

export class ExtensionSettingsStore implements SettingsStore {
  constructor(private readonly storage: StoragePort) {}

  async get(): Promise<Settings> {
    const rawSettings = await this.storage.get<unknown>(SETTINGS_KEY);
    return normalizeSettings(rawSettings);
  }

  async save(settings: Settings): Promise<void> {
    const normalized = normalizeSettings(settings);
    await this.storage.set(SETTINGS_KEY, normalized);
  }
}
