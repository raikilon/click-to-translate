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
import { FirefoxAuthFlow } from "./impl/authFlow";
import { FirefoxAuthSessionStore } from "./impl/authSessionStore";
import { SystemClock } from "./impl/clock";
import { FirefoxSettingsStore } from "./impl/settingsStore";

export interface CompositionRoot {
  settingsStore: FirefoxSettingsStore;
  authSessionStore: FirefoxAuthSessionStore;
  useCases: {
    getSettings: GetSettingsUseCase;
    saveSettings: SaveSettingsUseCase;
    login: LoginUseCase;
    logout: LogoutUseCase;
    loadLanguages: GetSelectableLanguagesUseCase;
    handleTrigger: HandleTranslateTriggerUseCase;
  };
}

export function createCompositionRoot(): CompositionRoot {
  const settingsStore = new FirefoxSettingsStore();
  const authSessionStore = new FirefoxAuthSessionStore();
  const clock = new SystemClock();
  const authFlow = new FirefoxAuthFlow(settingsStore, clock);
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
