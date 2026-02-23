import type { SettingsStore } from "@application";
import { DEFAULT_SETTINGS, type Settings } from "@application";
import type { StoragePort } from "../platform/BrowserAdapter";

const SETTINGS_KEY = "settings";

function normalizeSettings(candidate: unknown, baseDefaults: Settings): Settings {
  const baseSiteOverrides = Array.isArray(baseDefaults.siteOverrides)
    ? [...baseDefaults.siteOverrides]
    : baseDefaults.siteOverrides;

  const defaultClone: Settings = {
    ...baseDefaults,
    scopes: [...baseDefaults.scopes],
    modifiers: { ...baseDefaults.modifiers },
    siteOverrides: baseSiteOverrides,
  };

  if (!candidate || typeof candidate !== "object") {
    return defaultClone;
  }

  const value = candidate as Partial<Settings>;
  const rawModifiers =
    value.modifiers && typeof value.modifiers === "object"
      ? value.modifiers
      : undefined;

  return {
    ...defaultClone,
    ...value,
    scopes: Array.isArray(value.scopes)
      ? value.scopes.filter((scope): scope is string => typeof scope === "string")
      : [...defaultClone.scopes],
    modifiers: {
      ...defaultClone.modifiers,
      ...rawModifiers,
    },
    siteOverrides: Array.isArray(value.siteOverrides)
      ? [...value.siteOverrides]
      : defaultClone.siteOverrides,
  };
}

export class ExtensionSettingsStore implements SettingsStore {
  private readonly defaults: Settings;

  constructor(
    private readonly storage: StoragePort,
    defaultSettings?: Partial<Settings>,
  ) {
    this.defaults = normalizeSettings(defaultSettings, DEFAULT_SETTINGS);
  }

  async get(): Promise<Settings> {
    const rawSettings = await this.storage.get<unknown>(SETTINGS_KEY);
    return normalizeSettings(rawSettings, this.defaults);
  }

  async save(settings: Settings): Promise<void> {
    const normalized = normalizeSettings(settings, this.defaults);
    await this.storage.set(SETTINGS_KEY, normalized);
  }
}
