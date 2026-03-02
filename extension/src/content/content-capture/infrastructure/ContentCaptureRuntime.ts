import type { IContentCaptureRuntime } from "@/content/content-capture/application/IContentCaptureRuntime";
import { SubtitleBufferFeeder } from "./SubtitleBufferFeeder";

export class ContentCaptureRuntime implements IContentCaptureRuntime {
  constructor(private readonly subtitleBufferFeeder: SubtitleBufferFeeder) {}

  start(): void {
    this.subtitleBufferFeeder.start();
  }

  stop(): void {
    this.subtitleBufferFeeder.stop();
  }
}
