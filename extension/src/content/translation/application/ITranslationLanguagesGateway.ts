export interface ITranslationLanguagesGateway {
  listLanguages(accessToken: string): Promise<string[]>;
}
