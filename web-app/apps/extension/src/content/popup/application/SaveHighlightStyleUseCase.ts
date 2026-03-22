import type { IHighlightPrefsRepository } from "./IHighlightPrefsRepository";

export class SaveHighlightStyleUseCase {
  constructor(private readonly highlightPrefsRepository: IHighlightPrefsRepository) {}

  async execute(styleId: string): Promise<void> {
    await this.highlightPrefsRepository.saveHighlightStyleId(styleId);
  }
}

