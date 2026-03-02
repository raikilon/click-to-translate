import type { IHighlighter } from "./IHighlighter";

export class ClearHighlightUseCase {
  constructor(private readonly highlighter: IHighlighter) {}

  execute(): void {
    this.highlighter.clear();
  }
}
