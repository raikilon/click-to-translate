import type { Settings } from "@application";

export const INFRA_DEFAULT_SETTINGS: Partial<Settings> = {
  apiBaseUrl: "http://localhost:8080",
  languagesPath: "/api/translate/languages",
  segmentsPath: "/api/translate",
  authAuthorizeUrl:
    "http://localhost:8081/realms/click-to-translate/protocol/openid-connect/auth",
  authTokenUrl:
    "http://localhost:8081/realms/click-to-translate/protocol/openid-connect/token",
  oauthClientId: "click-to-translate-extension",
  scopes: ["openid", "segment", "translate"],
};
