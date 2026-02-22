import { ExtensionAuthSessionStore } from "@infra-shared/impl/authSessionStore";
import { firefoxAdapter } from "../platform/firefoxAdapter";

export class FirefoxAuthSessionStore extends ExtensionAuthSessionStore {
  constructor() {
    super(firefoxAdapter.storage);
  }
}
