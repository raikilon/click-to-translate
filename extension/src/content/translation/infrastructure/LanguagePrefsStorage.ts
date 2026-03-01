import { DEFAULT_LANGUAGE_PREFS, type LanguagePrefs } from "@/content/translation/domain/LanguagePrefs";
import { storage } from "wxt/utils/storage";

export const languagePrefsStorageItem =
  storage.defineItem<LanguagePrefs>("local:languagePrefs", {
    fallback: DEFAULT_LANGUAGE_PREFS,
  });




