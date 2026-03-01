import type { IContentCaptureRuntime } from "./IContentCaptureRuntime";

export class StartContentCaptureUseCase {
  constructor(private readonly runtime: IContentCaptureRuntime) {}

  execute(): void {
    this.runtime.start();
  }
}
