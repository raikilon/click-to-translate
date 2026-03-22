import type { HighlightSelection } from "@/content/popup/domain/HighlightSelection";

export interface IHighlighter {
  show(selection: HighlightSelection, styleId: string): void;
  clear(): void;
}





