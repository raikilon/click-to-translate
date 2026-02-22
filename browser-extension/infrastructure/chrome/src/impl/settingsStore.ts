import { ExtensionSettingsStore } from "@infra-shared/impl/settingsStore";
import { chromeAdapter } from "../platform/chromeAdapter";

export class ChromeSettingsStore extends ExtensionSettingsStore {
  constructor() {
    super(chromeAdapter.storage);
  }
}
