import type { Anchor } from "@domain";
import { tooltipRenderer } from "./tooltipRenderer";
import { videoOverlayRenderer } from "./videoOverlayRenderer";

export class PageRenderer {
  async render(anchor: Anchor, text: string): Promise<void> {
    if (this.shouldUseVideoOverlayForCurrentUrl()) {
      videoOverlayRenderer.show(anchor, text);
      return;
    }

    tooltipRenderer.render(anchor, text);
  }

  async dismiss(): Promise<void> {
    tooltipRenderer.dismiss();
    videoOverlayRenderer.hide();
  }

  private shouldUseVideoOverlayForCurrentUrl(): boolean {
    const url = window.location.href;
    return (
      url.includes("youtube.com/watch") ||
      url.includes("youtu.be/") ||
      url.includes("netflix.com/watch")
    );
  }
}
