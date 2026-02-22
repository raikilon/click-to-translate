import type { SettingsStore } from "../contracts/SettingsStore";
import type { Settings } from "../model/Settings";

export class GetSettingsUseCase {
  constructor(private readonly settingsStore: SettingsStore) {}

  execute(): Promise<Settings> {
    return this.settingsStore.get();
  }
}