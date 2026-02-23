export type MouseButton = "left" | "middle" | "right";

export interface SettingsModifiers {
  alt: boolean;
  ctrl: boolean;
  shift: boolean;
  meta: boolean;
}

export interface SiteOverride {
  pattern: string;
  strategyId: "generic" | "youtube" | "netflix";
}

export interface Settings {
  apiBaseUrl: string;
  languagesPath: string;
  segmentsPath: string;
  authAuthorizeUrl: string;
  authTokenUrl: string;
  oauthClientId: string;
  scopes: string[];
  registerUrl?: string;
  sourceLanguageId: string | null;
  targetLanguageId: string | null;
  mouseButton: MouseButton;
  modifiers: SettingsModifiers;
  siteOverrides?: SiteOverride[];
  showTooltip: boolean;
}

export const DEFAULT_SETTINGS: Settings = {
  apiBaseUrl: "",
  languagesPath: "",
  segmentsPath: "",
  authAuthorizeUrl: "",
  authTokenUrl: "",
  oauthClientId: "",
  scopes: [],
  sourceLanguageId: null,
  targetLanguageId: null,
  mouseButton: "left",
  modifiers: {
    alt: true,
    ctrl: false,
    shift: false,
    meta: false,
  },
  showTooltip: true,
};
