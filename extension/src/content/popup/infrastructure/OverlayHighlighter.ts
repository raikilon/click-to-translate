import type { IHighlighter } from "@/content/popup/application/IHighlighter";
import type { HighlightSelection } from "@/content/popup/domain/HighlightSelection";

const HIGHLIGHT_HOST_ID = "click-to-translate-highlight-host";

export class OverlayHighlighter implements IHighlighter {
  private host: HTMLDivElement | undefined;

  show(selection: HighlightSelection, styleId: string): void {
    this.ensureHost();
    if (!this.host) {
      return;
    }

    const rect = this.measure(selection);
    if (!rect) {
      this.clear();
      return;
    }

    const style = this.resolveStyle(styleId);
    this.host.style.display = "block";
    this.host.style.left = `${Math.round(rect.left)}px`;
    this.host.style.top = `${Math.round(rect.top)}px`;
    this.host.style.width = `${Math.max(1, Math.round(rect.width))}px`;
    this.host.style.height = `${Math.max(1, Math.round(rect.height))}px`;
    this.host.style.background = style.background;
    this.host.style.outline = style.outline;
  }

  clear(): void {
    if (!this.host) {
      return;
    }

    this.host.style.display = "none";
  }

  private ensureHost(): void {
    if (this.host) {
      return;
    }

    const existing = document.getElementById(HIGHLIGHT_HOST_ID) as HTMLDivElement | undefined;
    if (existing) {
      this.host = existing;
      return;
    }

    const host = document.createElement("div");
    host.id = HIGHLIGHT_HOST_ID;
    host.style.position = "fixed";
    host.style.left = "0";
    host.style.top = "0";
    host.style.display = "none";
    host.style.zIndex = "2147483646";
    host.style.pointerEvents = "none";
    host.style.borderRadius = "4px";

    document.documentElement.appendChild(host);
    this.host = host;
  }

  private measure(selection: HighlightSelection): DOMRect | undefined {
    const range = document.createRange();
    range.setStart(selection.textNode, selection.start);
    range.setEnd(selection.textNode, selection.end);
    const rect = range.getBoundingClientRect();

    if (!rect.width && !rect.height) {
      return undefined;
    }

    return rect;
  }

  private resolveStyle(styleId: string): { background: string; outline: string } {
    if (styleId === "solid") {
      return {
        background: "rgba(245, 158, 11, 0.35)",
        outline: "1px solid rgba(245, 158, 11, 0.85)",
      };
    }

    return {
      background: "rgba(59, 130, 246, 0.22)",
      outline: "1px solid rgba(59, 130, 246, 0.8)",
    };
  }
}





