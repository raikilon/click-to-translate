import {
  EnsureAuthSessionUseCase,
  GetSelectableLanguagesUseCase,
  GetSettingsUseCase,
  HandleTranslateTriggerUseCase,
  LoginUseCase,
  LogoutUseCase,
  SaveSettingsUseCase,
} from "@application";
import { HttpApiClient } from "./impl/apiClient";
import { ExtensionAuthFlow } from "./impl/authFlow";
import { ExtensionAuthSessionStore } from "./impl/authSessionStore";
import { SystemClock } from "./impl/clock";
import { ExtensionSettingsStore } from "./impl/settingsStore";
import type { BrowserAdapter } from "./platform/BrowserAdapter";

export interface CompositionRoot {
  settingsStore: ExtensionSettingsStore;
  authSessionStore: ExtensionAuthSessionStore;
  useCases: {
    getSettings: GetSettingsUseCase;
    saveSettings: SaveSettingsUseCase;
    login: LoginUseCase;
    logout: LogoutUseCase;
    loadLanguages: GetSelectableLanguagesUseCase;
    handleTrigger: HandleTranslateTriggerUseCase;
  };
}

export function createCompositionRoot(adapter: BrowserAdapter): CompositionRoot {
  const settingsStore = new ExtensionSettingsStore(adapter.storage);
  const authSessionStore = new ExtensionAuthSessionStore(adapter.storage);
  const clock = new SystemClock(adapter.nowMs);
  const authFlow = new ExtensionAuthFlow(settingsStore, clock, adapter.identity);
  const apiClient = new HttpApiClient(settingsStore);
  const ensureAuthSession = new EnsureAuthSessionUseCase(
    authSessionStore,
    authFlow,
    apiClient,
    clock,
  );

  const getSettings = new GetSettingsUseCase(settingsStore);
  const saveSettings = new SaveSettingsUseCase(settingsStore);
  const login = new LoginUseCase(ensureAuthSession);
  const logout = new LogoutUseCase(authSessionStore, authFlow);
  const loadLanguages = new GetSelectableLanguagesUseCase(
    settingsStore,
    apiClient,
    ensureAuthSession,
  );

  const handleTrigger = new HandleTranslateTriggerUseCase(
    settingsStore,
    apiClient,
    clock,
    ensureAuthSession,
  );

  return {
    settingsStore,
    authSessionStore,
    useCases: {
      getSettings,
      saveSettings,
      login,
      logout,
      loadLanguages,
      handleTrigger,
    },
  };
}
