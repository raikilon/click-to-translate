import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";
import type { ILookupTriggerPrefsRepository } from "./ILookupTriggerPrefsRepository";

export class SaveTriggerPrefsUseCase {
  constructor(private readonly repository: ILookupTriggerPrefsRepository) {}

  execute(prefs: TriggerPrefs): Promise<void> {
    return this.repository.set(prefs);
  }
}
