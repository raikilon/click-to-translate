import type { IBackgroundTranslationClient } from "./IBackgroundTranslationClient";

export class TranslateWordUseCase {
  constructor(private readonly backgroundTranslationClient: IBackgroundTranslationClient) {}

  execute(word: string, context: string): Promise<string | undefined> {
    return this.backgroundTranslationClient.translate(word, context);
  }
}
