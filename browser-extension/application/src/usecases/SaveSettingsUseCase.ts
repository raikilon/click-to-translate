import type { SettingsStore } from "../contracts/SettingsStore";
import type { Settings } from "../model/Settings";

export class SaveSettingsUseCase {
  constructor(private readonly settingsStore: SettingsStore) {}

  async execute(settings: Settings): Promise<Settings> {
    await this.settingsStore.save(settings);
    return this.settingsStore.get();
  }
}