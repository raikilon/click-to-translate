import { SubtitleEvent } from "@/content/content-capture/domain/SubtitleEvent";
import type { ISubtitleWatcher } from "./ISubtitleWatcher";

const NETFLIX_SUBTITLE_SELECTOR = ".player-timedtext .player-timedtext-text-container";

export class NetflixSubtitleWatcher implements ISubtitleWatcher {
  private observer: MutationObserver | undefined;
  private onEvent: ((event: SubtitleEvent) => void) | undefined;
  private lastText = "";

  start(onEvent: (event: SubtitleEvent) => void): void {
    this.onEvent = onEvent;
    if (!this.matchesUrl(window.location.href) || this.observer) {
      return;
    }

    this.captureVisible();

    this.observer = new MutationObserver((mutations) => {
      for (const mutation of mutations) {
        this.captureNode(mutation.target);
        for (const addedNode of mutation.addedNodes) {
          this.captureNode(addedNode);
        }
      }
    });

    this.observer.observe(document.body, {
      subtree: true,
      childList: true,
      characterData: true,
    });
  }

  stop(): void {
    this.observer?.disconnect();
    this.observer = undefined;
    this.onEvent = undefined;
    this.lastText = "";
  }

  private matchesUrl(url: string): boolean {
    return url.includes("netflix.com/watch");
  }

  private captureVisible(): void {
    for (const element of document.querySelectorAll(NETFLIX_SUBTITLE_SELECTOR)) {
      this.captureElement(element);
    }
  }

  private captureNode(node: Node): void {
    if (node instanceof Element) {
      this.captureTree(node);
      return;
    }

    if (node instanceof Text && node.parentElement) {
      this.captureTree(node.parentElement);
    }
  }

  private captureTree(element: Element): void {
    const elements = new Set<Element>();

    if (element.matches(NETFLIX_SUBTITLE_SELECTOR)) {
      elements.add(element);
    }

    for (const found of element.querySelectorAll(NETFLIX_SUBTITLE_SELECTOR)) {
      elements.add(found);
    }

    for (const found of elements) {
      this.captureElement(found);
    }
  }

  private captureElement(element: Element): void {
    if (!(element instanceof HTMLElement) || !this.onEvent || !this.isVisible(element)) {
      return;
    }

    const text = (element.innerText || element.textContent || "").replace(/\s+/g, " ").trim();
    if (!text || text === this.lastText) {
      return;
    }

    this.lastText = text;
    this.onEvent(new SubtitleEvent(text, Date.now()));
  }

  private isVisible(element: HTMLElement): boolean {
    const style = window.getComputedStyle(element);
    if (style.display === "none" || style.visibility === "hidden") {
      return false;
    }

    const rect = element.getBoundingClientRect();
    return rect.width > 0 && rect.height > 0;
  }
}





