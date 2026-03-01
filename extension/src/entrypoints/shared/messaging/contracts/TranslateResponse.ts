export interface TranslateResponse {
  kind: "ok" | "missingLanguagePrefs" | "unauthenticated" | "error";
  translation?: string;
  message?: string;
}





