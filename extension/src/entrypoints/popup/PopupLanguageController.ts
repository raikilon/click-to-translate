import type { LanguagePrefs } from "@domain";
import type { BackgroundClient } from "../shared/messaging/client";
import { PopupPrefsController } from "./PopupPrefsController";

export class PopupLanguageController {
  constructor(
    private readonly client: BackgroundClient,
    private readonly prefsController: PopupPrefsController,
  ) {}

  listAvailableLanguageIds(): Promise<string[]> {
    return this.client.getTranslationLanguages();
  }

  loadLanguagePrefs(): Promise<LanguagePrefs> {
    return this.prefsController.getLanguagePrefs();
  }

  async saveLanguagePrefs(prefs: LanguagePrefs): Promise<void> {
    await this.prefsController.setLanguagePrefs(prefs);
  }
}
