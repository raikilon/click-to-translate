import type { ITranslationPopup } from "./ITranslationPopup";

export class ClearTranslationPopupUseCase {
  constructor(private readonly translationPopup: ITranslationPopup) {}

  execute(): void {
    this.translationPopup.clear();
  }
}
