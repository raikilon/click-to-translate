import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";

export interface IPrefsRepository {
  getTriggerPrefs(): Promise<TriggerPrefs>;
  getHoverDelayMs(): Promise<number>;
}





