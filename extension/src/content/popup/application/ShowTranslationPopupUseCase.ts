import type { ITranslationPopup } from "./ITranslationPopup";
import type { TranslationPopupContent } from "@/content/popup/domain/TranslationPopupContent";

export class ShowTranslationPopupUseCase {
  constructor(private readonly translationPopup: ITranslationPopup) {}

  execute(content: TranslationPopupContent): void {
    this.translationPopup.show(content);
  }
}
