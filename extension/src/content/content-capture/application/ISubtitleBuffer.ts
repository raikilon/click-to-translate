import type { SubtitleEvent } from "@/content/content-capture/domain/SubtitleEvent";

export interface ISubtitleBuffer {
  append(event: SubtitleEvent): void;
  getJoinedText(nowMs?: number): string;
  hasRecentEntries(nowMs?: number): boolean;
}





