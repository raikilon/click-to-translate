import type { Anchor } from "@/content/content-capture/domain/Anchor";

export class ContextMatch {
  constructor(
    readonly word: string,
    readonly context: string,
    readonly anchor: Anchor,
  ) {}
}





