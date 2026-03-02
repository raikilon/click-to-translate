import type { ILookupRuntime } from "./ILookupRuntime";

export class StartLookupUseCase {
  constructor(private readonly runtime: ILookupRuntime) {}

  execute(): void {
    this.runtime.start();
  }
}
