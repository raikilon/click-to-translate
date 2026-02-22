import {
  EnsureAuthSessionUseCase,
  GetSelectableLanguagesUseCase,
  GetSettingsUseCase,
  HandleTranslateTriggerUseCase,
  LoginUseCase,
  LogoutUseCase,
  SaveSettingsUseCase,
  type PageProbe,
  type RenderPayload,
  type Renderer,
} from "@application";
import type {
  DisplayInstruction,
  SelectionSnapshot,
  SubtitleSnapshot,
  TextAtPoint,
  Trigger,
} from "@domain";
import { HttpApiClient } from "./impl/apiClient";
import { FirefoxAuthFlow } from "./impl/authFlow";
import { FirefoxAuthSessionStore } from "./impl/authSessionStore";
import { SystemClock } from "./impl/clock";
import { FirefoxSettingsStore } from "./impl/settingsStore";

class NoopRenderer implements Renderer {
  async render(
    _instruction: DisplayInstruction,
    _payload: RenderPayload,
  ): Promise<void> {}

  async dismiss(): Promise<void> {}
}

class NoopPageProbe implements PageProbe {
  async getPageInfo(): Promise<{ url: string }> {
    return { url: "" };
  }

  async getSelectionSnapshot(_trigger: Trigger): Promise<SelectionSnapshot | null> {
    return null;
  }

  async getTextAtPoint(_trigger: Trigger): Promise<TextAtPoint | null> {
    return null;
  }

  async getSubtitleSnapshot(_trigger: Trigger): Promise<SubtitleSnapshot | null> {
    return null;
  }
}

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
    new NoopPageProbe(),
    apiClient,
    new NoopRenderer(),
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
