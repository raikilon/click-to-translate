import type { ITranslationPopup } from "@/content/popup/application/ITranslationPopup";
import type { TranslationPopupContent } from "@/content/popup/domain/TranslationPopupContent";

const POPUP_HOST_ID = "click-to-translate-translation-popup-host";
const OFFSET_PX = 10;

export class TranslationPopup implements ITranslationPopup {
  private host: HTMLDivElement | undefined;
  private bubble: HTMLDivElement | undefined;

  show(content: TranslationPopupContent): void {
    this.ensureDom();
    if (!this.bubble) {
      return;
    }

    this.bubble.textContent = content.translation;
    this.bubble.style.display = "block";

    const viewWidth = window.innerWidth;
    const viewHeight = window.innerHeight;

    let left = content.anchorX + OFFSET_PX;
    let top = content.anchorY + OFFSET_PX;

    this.bubble.style.left = `${left}px`;
    this.bubble.style.top = `${top}px`;

    const rect = this.bubble.getBoundingClientRect();
    if (rect.right > viewWidth - 8) {
      left = Math.max(8, viewWidth - rect.width - 8);
    }

    if (rect.bottom > viewHeight - 8) {
      top = Math.max(8, content.anchorY - rect.height - OFFSET_PX);
    }

    this.bubble.style.left = `${left}px`;
    this.bubble.style.top = `${top}px`;
  }

  clear(): void {
    if (this.bubble) {
      this.bubble.style.display = "none";
    }
  }

  private ensureDom(): void {
    if (this.host && this.bubble) {
      return;
    }

    const existingHost = document.getElementById(POPUP_HOST_ID) as HTMLDivElement | undefined;
    if (existingHost) {
      this.host = existingHost;
      this.bubble = existingHost.shadowRoot?.querySelector(
        '[data-role="popup"]',
      ) as HTMLDivElement | undefined;
      return;
    }

    const host = document.createElement("div");
    host.id = POPUP_HOST_ID;
    host.style.position = "fixed";
    host.style.left = "0";
    host.style.top = "0";
    host.style.pointerEvents = "none";
    host.style.zIndex = "2147483647";

    const shadow = host.attachShadow({ mode: "open" });
    const style = document.createElement("style");
    style.textContent = `
      .popup {
        display: none;
        position: fixed;
        pointer-events: auto;
        max-width: min(420px, calc(100vw - 24px));
        border-radius: 10px;
        border: 1px solid rgba(148, 163, 184, 0.45);
        background: rgba(15, 23, 42, 0.96);
        color: #f8fafc;
        font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, sans-serif;
        font-size: 13px;
        line-height: 1.45;
        padding: 10px 12px;
        box-shadow: 0 12px 28px rgba(0, 0, 0, 0.4);
        white-space: pre-wrap;
        word-break: break-word;
      }
    `;

    const bubble = document.createElement("div");
    bubble.className = "popup";
    bubble.dataset.role = "popup";

    shadow.appendChild(style);
    shadow.appendChild(bubble);
    document.documentElement.appendChild(host);

    this.host = host;
    this.bubble = bubble;
  }
}





