import type { SettingsStore } from "@application";
import { DEFAULT_SETTINGS, type Settings } from "@application";

const SETTINGS_KEY = "settings";

function getStorageLocal<T>(key: string): Promise<T | undefined> {
  return browser.storage.local
    .get(key)
    .then((items) => items[key] as T | undefined);
}

function setStorageLocal<T>(key: string, value: T): Promise<void> {
  return browser.storage.local.set({ [key]: value });
}

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

export class FirefoxSettingsStore implements SettingsStore {
  async get(): Promise<Settings> {
    const rawSettings = await getStorageLocal<unknown>(SETTINGS_KEY);
    return normalizeSettings(rawSettings);
  }

  async save(settings: Settings): Promise<void> {
    const normalized = normalizeSettings(settings);
    await setStorageLocal(SETTINGS_KEY, normalized);
  }
}
