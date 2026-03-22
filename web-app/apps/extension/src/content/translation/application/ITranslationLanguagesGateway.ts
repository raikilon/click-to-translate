export interface ITranslationLanguagesGateway {
  listLanguages(): Promise<string[]>;
}
