import type { IHighlighter } from "./IHighlighter";
import type { IHighlightPrefsRepository } from "./IHighlightPrefsRepository";
import type { HighlightSelection } from "@/content/popup/domain/HighlightSelection";

export class ShowHighlightUseCase {
  constructor(
    private readonly highlighter: IHighlighter,
    private readonly highlightPrefsRepository: IHighlightPrefsRepository,
  ) {}

  async execute(selection: HighlightSelection): Promise<void> {
    const styleId = await this.highlightPrefsRepository.getHighlightStyleId();
    this.highlighter.show(selection, styleId);
  }
}
