import type { ISubtitleBuffer } from "@/content/content-capture/application/ISubtitleBuffer";
import { SubtitleEvent } from "@/content/content-capture/domain/SubtitleEvent";
import type { ISubtitleWatcher } from "./ISubtitleWatcher";

export class SubtitleBufferFeeder {
  constructor(
    private readonly subtitleBuffer: ISubtitleBuffer,
    private readonly watchers: readonly ISubtitleWatcher[],
  ) {}

  start(): void {
    for (const watcher of this.watchers) {
      watcher.start(this.onEvent.bind(this));
    }
  }

  stop(): void {
    for (const watcher of this.watchers) {
      watcher.stop();
    }
  }

  private onEvent(event: SubtitleEvent): void {
    const text = event.text.replace(/\s+/g, " ").trim();
    if (!text) {
      return;
    }

    this.subtitleBuffer.append(new SubtitleEvent(text, event.capturedAtMs));
  }
}





