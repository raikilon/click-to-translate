import type { Clock, SettingsStore } from "@application";
import { ExtensionAuthFlow } from "@infra-shared/impl/authFlow";
import { firefoxAdapter } from "../platform/firefoxAdapter";

export class FirefoxAuthFlow extends ExtensionAuthFlow {
  constructor(settingsStore: SettingsStore, clock: Clock) {
    super(settingsStore, clock, firefoxAdapter.identity);
  }
}
