import { ExtensionSettingsStore } from "@infra-shared/impl/settingsStore";
import { firefoxAdapter } from "../platform/firefoxAdapter";

export class FirefoxSettingsStore extends ExtensionSettingsStore {
  constructor() {
    super(firefoxAdapter.storage);
  }
}
