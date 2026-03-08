import type { ITranslationLanguagesGateway } from "./ITranslationLanguagesGateway";

export class ListTranslationLanguagesUseCase {
  constructor(private readonly translationLanguagesGateway: ITranslationLanguagesGateway) {}

  async execute(): Promise<string[]> {
    return this.translationLanguagesGateway.listLanguages();
  }
}
