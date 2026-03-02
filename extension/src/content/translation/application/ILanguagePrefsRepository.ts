import type { LanguagePrefs } from "@/content/translation/domain/LanguagePrefs";

export interface ILanguagePrefsRepository {
  get(): Promise<LanguagePrefs>;
  set(prefs: LanguagePrefs): Promise<void>;
}
