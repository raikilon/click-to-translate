import { GetLanguagePrefsUseCase } from "@/content/translation/application/GetLanguagePrefsUseCase";
import { SaveLanguagePrefsUseCase } from "@/content/translation/application/SaveLanguagePrefsUseCase";
import type { LanguagePrefs } from "@/content/translation/domain/LanguagePrefs";

export class PopupPrefsController {
  constructor(
    private readonly getLanguagePrefsUseCase: GetLanguagePrefsUseCase,
    private readonly saveLanguagePrefsUseCase: SaveLanguagePrefsUseCase,
  ) {}

  async getLanguagePrefs(): Promise<LanguagePrefs> {
    return this.getLanguagePrefsUseCase.execute();
  }

  async setLanguagePrefs(prefs: LanguagePrefs): Promise<void> {
    await this.saveLanguagePrefsUseCase.execute(prefs);
  }
}





