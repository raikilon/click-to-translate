import type { Anchor } from "@domain";

const VIDEO_OVERLAY_HOST_ID = "click-to-translate-video-overlay-host";
const VIEWPORT_MARGIN_PX = 8;
const ANCHOR_OFFSET_PX = 10;

export interface VideoOverlayShowOptions {
  dismissOnOutsideClick?: boolean;
  dismissOnEscape?: boolean;
}

export class VideoOverlayRenderer {
  private host: HTMLDivElement | null = null;
  private bubble: HTMLDivElement | null = null;
  private cleanupListeners: Array<() => void> = [];

  show(anchor: Anchor, text: string, options: VideoOverlayShowOptions = {}): void {
    this.ensureDom();
    if (!this.host || !this.bubble) {
      return;
    }

    this.bubble.textContent = text;
    this.bubble.style.display = "block";
    this.positionBubble(anchor);

    const resolvedOptions = {
      dismissOnOutsideClick: options.dismissOnOutsideClick ?? true,
      dismissOnEscape: options.dismissOnEscape ?? true,
    };

    this.clearListeners();

    if (resolvedOptions.dismissOnOutsideClick) {
      const pointerHandler = (event: MouseEvent | PointerEvent) => {
        if (!this.host) {
          return;
        }

        if (event.composedPath().includes(this.host)) {
          return;
        }

        this.hide();
      };

      document.addEventListener("pointerdown", pointerHandler, true);
      this.cleanupListeners.push(() =>
        document.removeEventListener("pointerdown", pointerHandler, true),
      );
    }

    if (resolvedOptions.dismissOnEscape) {
      const keyHandler = (event: KeyboardEvent) => {
        if (event.key === "Escape") {
          this.hide();
        }
      };

      document.addEventListener("keydown", keyHandler);
      this.cleanupListeners.push(() =>
        document.removeEventListener("keydown", keyHandler),
      );
    }
  }

  hide(): void {
    this.clearListeners();

    if (this.bubble) {
      this.bubble.style.display = "none";
    }
  }

  private ensureDom(): void {
    if (this.host && this.bubble) {
      return;
    }

    const existingHost = document.getElementById(
      VIDEO_OVERLAY_HOST_ID,
    ) as HTMLDivElement | null;
    if (existingHost) {
      this.host = existingHost;
      this.bubble = existingHost.shadowRoot?.querySelector(
        '[data-role="video-overlay"]',
      ) as HTMLDivElement | null;
      return;
    }

    const host = document.createElement("div");
    host.id = VIDEO_OVERLAY_HOST_ID;
    host.style.position = "fixed";
    host.style.left = "0";
    host.style.top = "0";
    host.style.zIndex = "2147483647";
    host.style.pointerEvents = "none";

    const shadow = host.attachShadow({ mode: "open" });
    const style = document.createElement("style");
    style.textContent = `
      :host {
        all: initial;
      }
      .overlay {
        position: fixed;
        display: none;
        pointer-events: auto;
        max-width: min(560px, calc(100vw - 24px));
        padding: 10px 14px;
        border-radius: 10px;
        border: 1px solid rgba(255, 255, 255, 0.35);
        background: rgba(17, 24, 39, 0.92);
        color: #f9fafb;
        box-shadow: 0 14px 34px rgba(0, 0, 0, 0.5);
        font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, sans-serif;
        font-size: 14px;
        line-height: 1.45;
        white-space: pre-wrap;
        word-break: break-word;
        text-wrap: pretty;
      }
    `;

    const bubble = document.createElement("div");
    bubble.className = "overlay";
    bubble.dataset.role = "video-overlay";

    shadow.appendChild(style);
    shadow.appendChild(bubble);
    document.documentElement.appendChild(host);

    this.host = host;
    this.bubble = bubble;
  }

  private positionBubble(anchor: Anchor): void {
    if (!this.bubble) {
      return;
    }

    this.bubble.style.left = "-99999px";
    this.bubble.style.top = "-99999px";
    const overlayRect = this.bubble.getBoundingClientRect();
    const viewWidth = window.innerWidth;
    const viewHeight = window.innerHeight;

    let nextX = VIEWPORT_MARGIN_PX;
    let nextY = VIEWPORT_MARGIN_PX;

    if (anchor.kind === "rect") {
      const preferredCenterX = anchor.x + anchor.w / 2;
      nextX = preferredCenterX - overlayRect.width / 2;
      nextX = Math.max(
        VIEWPORT_MARGIN_PX,
        Math.min(nextX, viewWidth - overlayRect.width - VIEWPORT_MARGIN_PX),
      );

      const aboveY = anchor.y - overlayRect.height - ANCHOR_OFFSET_PX;
      const belowY = anchor.y + anchor.h + ANCHOR_OFFSET_PX;
      if (aboveY >= VIEWPORT_MARGIN_PX) {
        nextY = aboveY;
      } else if (belowY + overlayRect.height <= viewHeight - VIEWPORT_MARGIN_PX) {
        nextY = belowY;
      } else {
        nextY = Math.max(
          VIEWPORT_MARGIN_PX,
          Math.min(
            viewHeight - overlayRect.height - VIEWPORT_MARGIN_PX,
            anchor.y + anchor.h / 2 - overlayRect.height / 2,
          ),
        );
      }
    } else {
      nextX = anchor.x + ANCHOR_OFFSET_PX;
      nextY = anchor.y + ANCHOR_OFFSET_PX;

      if (nextX + overlayRect.width > viewWidth - VIEWPORT_MARGIN_PX) {
        nextX = Math.max(
          VIEWPORT_MARGIN_PX,
          anchor.x - overlayRect.width - ANCHOR_OFFSET_PX,
        );
      }

      if (nextY + overlayRect.height > viewHeight - VIEWPORT_MARGIN_PX) {
        nextY = Math.max(
          VIEWPORT_MARGIN_PX,
          anchor.y - overlayRect.height - ANCHOR_OFFSET_PX,
        );
      }
    }

    this.bubble.style.left = `${Math.round(nextX)}px`;
    this.bubble.style.top = `${Math.round(nextY)}px`;
  }

  private clearListeners(): void {
    for (const cleanup of this.cleanupListeners) {
      cleanup();
    }

    this.cleanupListeners = [];
  }
}

export const videoOverlayRenderer = new VideoOverlayRenderer();

