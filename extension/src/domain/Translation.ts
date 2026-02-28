export interface TranslationRequest {
  selectedWord: string;
  textAround: string;
  sourceLanguageId: string;
  targetLanguageId: string;
}

export interface TranslationResponse {
  translatedText?: string;
}
