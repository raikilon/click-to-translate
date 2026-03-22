export interface IBackgroundTranslationClient {
  translate(word: string, context: string): Promise<string | undefined>;
}





