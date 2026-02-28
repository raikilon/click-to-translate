import { languagePrefsStorageItem } from "../background/storage/items";
import type { LanguagePrefs } from "@domain";

export class PopupPrefsController {
  async getLanguagePrefs(): Promise<LanguagePrefs> {
    return languagePrefsStorageItem.getValue();
  }

  async setLanguagePrefs(prefs: LanguagePrefs): Promise<void> {
    await languagePrefsStorageItem.setValue(prefs);
  }
}
