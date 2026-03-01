interface TranslationRuntimeConfig {
  apiBaseUrl: string;
  translatePath: string;
  translateLanguagesPath: string;
}

export class TranslationRuntimeConfigFactory {
  static create(): TranslationRuntimeConfig {
    return {
      apiBaseUrl: this.readRequiredEnvString("WXT_API_BASE_URL"),
      translatePath: this.readRequiredEnvString("WXT_TRANSLATE_PATH"),
      translateLanguagesPath:
        this.readEnvString("WXT_TRANSLATE_LANGUAGES_PATH") ?? "/api/translate/languages",
    };
  }

  private static readEnvString(name: string): string | undefined {
    const value = (import.meta.env[name] as string | undefined)?.trim();
    return value || undefined;
  }

  private static readRequiredEnvString(name: string): string {
    const value = this.readEnvString(name);
    if (!value) {
      throw new Error(`Missing required runtime config env var: ${name}`);
    }

    return value;
  }
}

export const translationRuntimeConfig: TranslationRuntimeConfig =
  TranslationRuntimeConfigFactory.create();
