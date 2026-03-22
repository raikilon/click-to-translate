import type { ILookupTriggerPrefsRepository } from "@/content/lookup/application/ILookupTriggerPrefsRepository";
import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";
import { triggerPrefsStorageItem } from "./LookupPrefsStorage";

export class LookupTriggerPrefsRepository implements ILookupTriggerPrefsRepository {
  get(): Promise<TriggerPrefs> {
    return triggerPrefsStorageItem.getValue();
  }

  set(prefs: TriggerPrefs): Promise<void> {
    return triggerPrefsStorageItem.setValue(prefs);
  }
}
