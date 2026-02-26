import type { Settings } from "@application";

declare const __EXT_PROFILE__: "prod" | "dev";

const COMMON_DEFAULT_SETTINGS: Partial<Settings> = {
  languagesPath: "/api/translate/languages",
  segmentsPath: "/api/translate",
  scopes: ["openid", "segment", "translate"],
};

const PROD_DEFAULT_SETTINGS: Partial<Settings> = {
  ...COMMON_DEFAULT_SETTINGS,
  apiBaseUrl: "replace-this-in-profile",
  authAuthorizeUrl: "replace-this-in-profile",
  authTokenUrl: "replace-this-in-profile",
  oauthClientId: "replace-this-in-profile",
};

const DEV_DEFAULT_SETTINGS: Partial<Settings> = {
  ...COMMON_DEFAULT_SETTINGS,
  apiBaseUrl: "http://localhost:8080",
  authAuthorizeUrl:
    "http://localhost:8081/realms/click-to-translate/protocol/openid-connect/auth",
  authTokenUrl:
    "http://localhost:8081/realms/click-to-translate/protocol/openid-connect/token",
  oauthClientId: "click-to-translate-extension",
};

export const INFRA_DEFAULT_SETTINGS: Partial<Settings> =
  __EXT_PROFILE__ === "dev" ? DEV_DEFAULT_SETTINGS : PROD_DEFAULT_SETTINGS;
