import type { ILookupRuntime } from "./ILookupRuntime";

export class StopLookupUseCase {
  constructor(private readonly runtime: ILookupRuntime) {}

  execute(): void {
    this.runtime.stop();
  }
}
