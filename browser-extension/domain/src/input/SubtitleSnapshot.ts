import type { Anchor } from "./Anchor";

export interface SubtitleSnapshot {
  text: string;
  anchor: Anchor;
  provider: "youtube" | "netflix" | "unknown";
}
