import type { LanguageId } from '@vocabulary/language';

export interface LanguagePrefs {
  sourceLanguageId: LanguageId | undefined;
  targetLanguageId: LanguageId | undefined;
}

export const DEFAULT_LANGUAGE_PREFS: LanguagePrefs = {
  sourceLanguageId: undefined,
  targetLanguageId: undefined,
};





