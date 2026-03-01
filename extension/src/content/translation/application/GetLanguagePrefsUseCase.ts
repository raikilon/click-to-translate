import type { LanguagePrefs } from "@/content/translation/domain/LanguagePrefs";
import type { ILanguagePrefsRepository } from "./ILanguagePrefsRepository";

export class GetLanguagePrefsUseCase {
  constructor(private readonly languagePrefsRepository: ILanguagePrefsRepository) {}

  execute(): Promise<LanguagePrefs> {
    return this.languagePrefsRepository.get();
  }
}
