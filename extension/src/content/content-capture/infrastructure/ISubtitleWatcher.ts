import type { SubtitleEvent } from "@/content/content-capture/domain/SubtitleEvent";

export interface ISubtitleWatcher {
  start(onEvent: (event: SubtitleEvent) => void): void;
  stop(): void;
}





