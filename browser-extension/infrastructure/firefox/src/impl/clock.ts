import type { Clock } from "@application";

export class SystemClock implements Clock {
  nowMs(): number {
    return Date.now();
  }
}
