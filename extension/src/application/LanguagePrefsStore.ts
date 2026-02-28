import type { LanguagePrefs } from "@domain";

export interface LanguagePrefsStore {
  get(): Promise<LanguagePrefs>;
  set(prefs: LanguagePrefs): Promise<void>;
}
