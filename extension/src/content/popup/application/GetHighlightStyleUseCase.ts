import type { IHighlightPrefsRepository } from "./IHighlightPrefsRepository";

export class GetHighlightStyleUseCase {
  constructor(private readonly highlightPrefsRepository: IHighlightPrefsRepository) {}

  execute(): Promise<string> {
    return this.highlightPrefsRepository.getHighlightStyleId();
  }
}

