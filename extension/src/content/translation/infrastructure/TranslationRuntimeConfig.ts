interface TranslationRuntimeConfig {
  apiBaseUrl: string;
  segmentPath: string;
  translateLanguagesPath: string;
}

export class TranslationRuntimeConfigFactory {
  static create(): TranslationRuntimeConfig {
    return {
      apiBaseUrl: this.readRequiredEnvString("WXT_API_BASE_URL"),
      segmentPath: this.readRequiredEnvString("WXT_SEGMENT_PATH"),
      translateLanguagesPath: this.readRequiredEnvString(
        "WXT_TRANSLATE_LANGUAGES_PATH",
      ),
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
