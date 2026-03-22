import type { IContentCaptureRuntime } from "./IContentCaptureRuntime";

export class StopContentCaptureUseCase {
  constructor(private readonly runtime: IContentCaptureRuntime) {}

  execute(): void {
    this.runtime.stop();
  }
}
