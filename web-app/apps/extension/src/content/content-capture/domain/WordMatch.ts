import type { Anchor } from "@/content/content-capture/domain/Anchor";

export class WordMatch {
  constructor(
    readonly word: string,
    readonly textNode: Text,
    readonly sourceText: string,
    readonly start: number,
    readonly end: number,
    readonly anchor: Anchor,
  ) {}
}





