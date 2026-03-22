import type { ITranslationGateway } from "./TranslationGateway";
import type { TranslateSelectionInput } from "@/content/translation/domain/TranslateSelectionInput";

export class TranslateSelectionUseCase {
  constructor(private readonly translationGateway: ITranslationGateway) {}

  async execute(input: TranslateSelectionInput): Promise<string | undefined> {
    const result = await this.translationGateway.translate({
      selectedWord: input.word,
      textAround: input.context,
      sourceLanguageId: input.sourceLanguageId,
      targetLanguageId: input.targetLanguageId,
    });

    const translatedText = result.translatedText?.trim();
    return translatedText || undefined;
  }
}
