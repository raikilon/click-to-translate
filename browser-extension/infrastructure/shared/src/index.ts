export type {
  BrowserAdapter,
  IdentityPort,
  RuntimeMessageSender,
  RuntimePort,
  StoragePort,
} from "./platform/BrowserAdapter";

export { createCompositionRoot } from "./compositionRoot";
export type { CompositionRoot } from "./compositionRoot";
export { registerBackground } from "./background/background";
export * from "./background/messageTypes";
export { registerContentScript } from "./content/contentScript";
export { registerPopup } from "./pages/popup";
export { registerOptions } from "./pages/options";
export { HttpApiClient, ApiHttpError } from "./impl/apiClient";
export { ExtensionAuthFlow } from "./impl/authFlow";
export { ExtensionAuthSessionStore } from "./impl/authSessionStore";
export { ExtensionSettingsStore } from "./impl/settingsStore";
export { SystemClock } from "./impl/clock";
export * from "./impl/pkce";
