import {
  type Anchor,
  CapturedClick,
  DomainValidationError,
  type ContextReducer,
} from "@domain";
import type { ITranslationGateway } from "./TranslationGateway";
import type { LanguagePrefsStore } from "./LanguagePrefsStore";

export type TranslateClickStatus =
  | "rendered"
  | "no_translation"
  | "missing_language_prefs"
  | "invalid_capture";

export interface TranslateClickResult {
  status: TranslateClickStatus;
  translatedText?: string;
  anchor?: Anchor;
  reason?: string;
}

export class TranslateClickUseCase {
  constructor(
    private readonly translationGateway: ITranslationGateway,
    private readonly prefsStore: LanguagePrefsStore,
    private readonly contextReducer: ContextReducer,
  ) {}

  async execute(capturedClick: CapturedClick): Promise<TranslateClickResult> {
    try {
      const reducedTextAround = this.contextReducer.reduce({
        selectedWord: capturedClick.selectedWord,
        textAround: capturedClick.textAround,
      });

      const prefs = await this.prefsStore.get();
      if (!prefs.sourceLanguageId || !prefs.targetLanguageId) {
        return {
          status: "missing_language_prefs",
          anchor: capturedClick.anchor,
        };
      }

      const translation = await this.translationGateway.translate({
        selectedWord: capturedClick.selectedWord,
        textAround: reducedTextAround,
        sourceLanguageId: prefs.sourceLanguageId,
        targetLanguageId: prefs.targetLanguageId,
      });

      const translatedText = translation.translatedText?.trim();
      if (!translatedText) {
        return {
          status: "no_translation",
          anchor: capturedClick.anchor,
        };
      }

      return {
        status: "rendered",
        translatedText,
        anchor: capturedClick.anchor,
      };
    } catch (error) {
      if (error instanceof DomainValidationError) {
        return {
          status: "invalid_capture",
          reason: error.message,
          anchor: capturedClick.anchor,
        };
      }

      return {
        status: "no_translation",
        reason: error instanceof Error ? error.message : "Translation failed.",
        anchor: capturedClick.anchor,
      };
    }
  }
}
