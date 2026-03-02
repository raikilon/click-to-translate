import type { CapturePoint } from "@/content/content-capture/domain/CapturePoint";

export class MouseIdleGate {
  private timeoutId: number | undefined;
  private delayMs = 500;
  private onIdle: ((point: CapturePoint) => void) | undefined;

  arm(delayMs: number, onIdle: (point: CapturePoint) => void): void {
    this.delayMs = Math.max(50, delayMs);
    this.onIdle = onIdle;
    this.clearTimeout();
  }

  onMouseMove(point: CapturePoint): void {
    if (!this.onIdle) {
      return;
    }

    this.clearTimeout();
    this.timeoutId = window.setTimeout(() => {
      this.timeoutId = undefined;
      if (this.onIdle) {
        this.onIdle(point);
      }
    }, this.delayMs);
  }

  disarm(): void {
    this.onIdle = undefined;
    this.clearTimeout();
  }

  private clearTimeout(): void {
    if (this.timeoutId !== undefined) {
      window.clearTimeout(this.timeoutId);
      this.timeoutId = undefined;
    }
  }
}





