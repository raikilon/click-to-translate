import {
  DEFAULT_LANGUAGE_PREFS,
  DEFAULT_TRIGGER_PREFS,
  type LanguagePrefs,
  type TriggerPrefs,
} from "@domain";
import { storage } from "wxt/utils/storage";

export const languagePrefsStorageItem =
  storage.defineItem<LanguagePrefs>("local:languagePrefs", {
    fallback: DEFAULT_LANGUAGE_PREFS,
  });

export const triggerPrefsStorageItem =
  storage.defineItem<TriggerPrefs>("local:triggerPrefs", {
    fallback: DEFAULT_TRIGGER_PREFS,
  });

export const authLogoutStorageItem =
  storage.defineItem<boolean>("local:authLogout", {
    fallback: false,
  });
