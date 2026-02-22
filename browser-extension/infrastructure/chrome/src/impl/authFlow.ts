import type { Clock, SettingsStore } from "@application";
import { ExtensionAuthFlow } from "@infra-shared/impl/authFlow";
import { chromeAdapter } from "../platform/chromeAdapter";

export class ChromeAuthFlow extends ExtensionAuthFlow {
  constructor(settingsStore: SettingsStore, clock: Clock) {
    super(settingsStore, clock, chromeAdapter.identity);
  }
}
