import type { LanguagePrefs } from "@/content/translation/domain/LanguagePrefs";
import type { ILanguagePrefsRepository } from "./ILanguagePrefsRepository";

export class SaveLanguagePrefsUseCase {
  constructor(private readonly languagePrefsRepository: ILanguagePrefsRepository) {}

  execute(prefs: LanguagePrefs): Promise<void> {
    return this.languagePrefsRepository.set(prefs);
  }
}
