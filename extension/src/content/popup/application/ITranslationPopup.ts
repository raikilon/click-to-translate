import type { TranslationPopupContent } from "@/content/popup/domain/TranslationPopupContent";

export interface ITranslationPopup {
  show(content: TranslationPopupContent): void;
  clear(): void;
}





