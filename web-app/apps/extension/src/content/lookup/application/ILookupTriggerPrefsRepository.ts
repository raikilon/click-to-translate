import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";

export interface ILookupTriggerPrefsRepository {
  get(): Promise<TriggerPrefs>;
  set(prefs: TriggerPrefs): Promise<void>;
}
