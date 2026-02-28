export interface LanguagePrefs {
  sourceLanguageId: string | undefined;
  targetLanguageId: string | undefined;
}

export const DEFAULT_LANGUAGE_PREFS: LanguagePrefs = {
  sourceLanguageId: undefined,
  targetLanguageId: undefined,
};
