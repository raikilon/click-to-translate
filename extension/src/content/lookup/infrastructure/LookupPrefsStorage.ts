import { DEFAULT_TRIGGER_PREFS, type TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";
import { storage } from "wxt/utils/storage";

export const triggerPrefsStorageItem =
  storage.defineItem<TriggerPrefs>("local:triggerPrefs", {
    fallback: DEFAULT_TRIGGER_PREFS,
  });

export const hoverDelayMsStorageItem =
  storage.defineItem<number>("local:hoverDelayMs", {
    fallback: 500,
  });




