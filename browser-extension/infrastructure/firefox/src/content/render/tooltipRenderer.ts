import type { RenderPayload } from "@application";
import type { Anchor, DisplayInstruction } from "@domain";

const TOOLTIP_HOST_ID = "click-to-translate-tooltip-host";
const TOOLTIP_OFFSET_PX = 12;

function anchorToPoint(anchor: Anchor): { x: number; y: number } {
  if (anchor.kind === "rect") {
    return {
      x: anchor.x + anchor.w / 2,
      y: anchor.y + anchor.h,
    };
  }

  return {
    x: anchor.x,
    y: anchor.y,
  };
}

export class TooltipRenderer {
  private host: HTMLDivElement | null = null;
  private root: ShadowRoot | null = null;
  private bubble: HTMLDivElement | null = null;
  private cleanupListeners: Array<() => void> = [];

  render(instruction: DisplayInstruction, payload: RenderPayload): void {
    if (instruction.mode === "POPUP_ONLY") {
      return;
    }

    this.ensureDom();
    if (!this.host || !this.bubble) {
      return;
    }

    this.bubble.textContent = payload.text;
    this.bubble.style.display = "block";

    const point = anchorToPoint(instruction.anchor);
    this.positionBubble(point.x, point.y);

    this.clearListeners();
    const dismissOn = {
      outsideClick: true,
      escape: true,
      scroll: false,
      ...instruction.dismissOn,
    };

    if (dismissOn.outsideClick) {
      const pointerHandler = (event: MouseEvent | PointerEvent) => {
        if (!this.host) {
          return;
        }

        if (event.composedPath().includes(this.host)) {
          return;
        }

        this.dismiss();
      };

      document.addEventListener("pointerdown", pointerHandler, true);
      this.cleanupListeners.push(() =>
        document.removeEventListener("pointerdown", pointerHandler, true),
      );
    }

    if (dismissOn.escape) {
      const keyHandler = (event: KeyboardEvent) => {
        if (event.key === "Escape") {
          this.dismiss();
        }
      };

      document.addEventListener("keydown", keyHandler);
      this.cleanupListeners.push(() =>
        document.removeEventListener("keydown", keyHandler),
      );
    }

    if (dismissOn.scroll) {
      const scrollHandler = () => this.dismiss();
      window.addEventListener("scroll", scrollHandler, true);
      this.cleanupListeners.push(() =>
        window.removeEventListener("scroll", scrollHandler, true),
      );
    }
  }

  dismiss(): void {
    this.clearListeners();

    if (this.bubble) {
      this.bubble.style.display = "none";
    }
  }

  private ensureDom(): void {
    if (this.host && this.root && this.bubble) {
      return;
    }

    const host = document.getElementById(TOOLTIP_HOST_ID) as HTMLDivElement | null;
    if (host) {
      this.host = host;
      this.root = host.shadowRoot;
      this.bubble = host.shadowRoot?.querySelector(
        '[data-role="tooltip"]',
      ) as HTMLDivElement | null;
      return;
    }

    const newHost = document.createElement("div");
    newHost.id = TOOLTIP_HOST_ID;
    newHost.style.position = "fixed";
    newHost.style.left = "0";
    newHost.style.top = "0";
    newHost.style.zIndex = "2147483647";
    newHost.style.pointerEvents = "none";

    const shadow = newHost.attachShadow({ mode: "open" });
    const style = document.createElement("style");
    style.textContent = `
      :host {
        all: initial;
      }
      .tooltip {
        font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, sans-serif;
        font-size: 13px;
        line-height: 1.4;
        color: #ffffff;
        background: rgba(15, 23, 42, 0.96);
        border: 1px solid rgba(148, 163, 184, 0.35);
        border-radius: 8px;
        box-shadow: 0 8px 20px rgba(0, 0, 0, 0.35);
        padding: 8px 10px;
        max-width: 300px;
        white-space: pre-wrap;
        word-break: break-word;
        display: none;
        position: fixed;
        pointer-events: auto;
      }
    `;

    const bubble = document.createElement("div");
    bubble.className = "tooltip";
    bubble.dataset.role = "tooltip";

    shadow.appendChild(style);
    shadow.appendChild(bubble);

    document.documentElement.appendChild(newHost);

    this.host = newHost;
    this.root = shadow;
    this.bubble = bubble;
  }

  private positionBubble(baseX: number, baseY: number): void {
    if (!this.bubble) {
      return;
    }

    const viewWidth = window.innerWidth;
    const viewHeight = window.innerHeight;

    let x = Math.min(Math.max(baseX + TOOLTIP_OFFSET_PX, 8), viewWidth - 8);
    let y = Math.min(Math.max(baseY + TOOLTIP_OFFSET_PX, 8), viewHeight - 8);

    this.bubble.style.left = `${x}px`;
    this.bubble.style.top = `${y}px`;

    const rect = this.bubble.getBoundingClientRect();
    if (rect.right > viewWidth - 8) {
      x = Math.max(8, viewWidth - rect.width - 8);
      this.bubble.style.left = `${x}px`;
    }

    if (rect.bottom > viewHeight - 8) {
      y = Math.max(8, baseY - rect.height - TOOLTIP_OFFSET_PX);
      this.bubble.style.top = `${y}px`;
    }
  }

  private clearListeners(): void {
    for (const cleanup of this.cleanupListeners) {
      cleanup();
    }

    this.cleanupListeners = [];
  }
}

export const tooltipRenderer = new TooltipRenderer();
