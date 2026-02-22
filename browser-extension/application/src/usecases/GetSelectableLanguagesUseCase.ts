import type { LanguageDto } from "@domain";
import type { ApiClient } from "../contracts/ApiClient";
import type { SettingsStore } from "../contracts/SettingsStore";
import type { EnsureAuthSessionUseCase } from "./EnsureAuthSessionUseCase";
import {
  findLanguageById,
  normalizeLanguageList,
} from "./LanguageUtils";

export interface GetSelectableLanguagesResult {
  languages: LanguageDto[];
  sourceLanguage: LanguageDto | null;
  targetLanguage: LanguageDto | null;
}

export class GetSelectableLanguagesUseCase {
  constructor(
    private readonly settingsStore: SettingsStore,
    private readonly apiClient: ApiClient,
    private readonly ensureAuthSession?: EnsureAuthSessionUseCase,
  ) {}

  async execute(): Promise<GetSelectableLanguagesResult> {
    const settings = await this.settingsStore.get();
    const accessToken = await this.resolveAccessToken();
    const rawLanguages = (await this.apiClient.getLanguages(
      accessToken,
    )) as unknown;
    const languages = normalizeLanguageList(rawLanguages);

    return {
      languages,
      sourceLanguage: findLanguageById(languages, settings.sourceLanguageId),
      targetLanguage: findLanguageById(languages, settings.targetLanguageId),
    };
  }

  private async resolveAccessToken(): Promise<string | null> {
    if (!this.ensureAuthSession) {
      return null;
    }

    const session = await this.ensureAuthSession.execute({ interactive: false });
    return session?.accessToken ?? null;
  }
}
