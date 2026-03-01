import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";
import type { ILookupTriggerPrefsRepository } from "./ILookupTriggerPrefsRepository";

export class GetTriggerPrefsUseCase {
  constructor(private readonly repository: ILookupTriggerPrefsRepository) {}

  execute(): Promise<TriggerPrefs> {
    return this.repository.get();
  }
}
