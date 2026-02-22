import type { Clock } from "@application";

export class SystemClock implements Clock {
  constructor(private readonly now: () => number = () => Date.now()) {}

  nowMs(): number {
    return this.now();
  }
}
