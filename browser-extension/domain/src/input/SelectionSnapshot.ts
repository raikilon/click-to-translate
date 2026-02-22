import type { Anchor } from "./Anchor";

export interface SelectionSnapshot {
  selectedText: string;
  anchor?: Anchor;
}
