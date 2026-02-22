import type { Anchor } from "./Anchor";

export interface TextAtPoint {
  word: string;
  surroundingText?: string;
  anchor: Anchor;
}
