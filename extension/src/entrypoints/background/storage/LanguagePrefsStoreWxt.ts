import {
  type LanguagePrefsStore,
} from "@application";
import type { LanguagePrefs } from "@domain";
import { languagePrefsStorageItem } from "./items";

export class LanguagePrefsStoreWxt implements LanguagePrefsStore {
  get(): Promise<LanguagePrefs> {
    return languagePrefsStorageItem.getValue();
  }

  async set(prefs: LanguagePrefs): Promise<void> {
    await languagePrefsStorageItem.setValue(prefs);
  }
}
