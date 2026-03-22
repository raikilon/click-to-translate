export class TranslateSelectionInput {
  constructor(
    readonly word: string,
    readonly context: string,
    readonly sourceLanguageId: string,
    readonly targetLanguageId: string,
  ) {}
}

