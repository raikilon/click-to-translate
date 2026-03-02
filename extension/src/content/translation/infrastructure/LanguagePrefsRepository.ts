import type { ILanguagePrefsRepository } from "@/content/translation/application/ILanguagePrefsRepository";
import type { LanguagePrefs } from "@/content/translation/domain/LanguagePrefs";
import { languagePrefsStorageItem } from "./LanguagePrefsStorage";

export class LanguagePrefsRepository implements ILanguagePrefsRepository {
  get(): Promise<LanguagePrefs> {
    return languagePrefsStorageItem.getValue();
  }

  set(prefs: LanguagePrefs): Promise<void> {
    return languagePrefsStorageItem.setValue(prefs);
  }
}
