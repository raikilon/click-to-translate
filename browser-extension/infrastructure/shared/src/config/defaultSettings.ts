import type { Settings } from "@application";

export const INFRA_DEFAULT_SETTINGS: Partial<Settings> = {
  apiBaseUrl: "http://localhost:8080",
  languagesPath: "/api/translate/languages",
  segmentsPath: "/api/translate",
  authAuthorizeUrl: "",
  authTokenUrl: "",
  oauthClientId: "",
  scopes: [],
};
