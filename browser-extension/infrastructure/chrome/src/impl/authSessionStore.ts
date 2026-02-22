import { ExtensionAuthSessionStore } from "@infra-shared/impl/authSessionStore";
import { chromeAdapter } from "../platform/chromeAdapter";

export class ChromeAuthSessionStore extends ExtensionAuthSessionStore {
  constructor() {
    super(chromeAdapter.storage);
  }
}
