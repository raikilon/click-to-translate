import { SubtitleEvent } from "@/content/content-capture/domain/SubtitleEvent";
import type { ISubtitleWatcher } from "./ISubtitleWatcher";

export class NetflixSubtitleWatcher implements ISubtitleWatcher {
  private observer: MutationObserver | undefined;
  private onEvent: ((event: SubtitleEvent) => void) | undefined;

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
  }

  private matchesUrl(url: string): boolean {
    return url.includes("netflix.com/watch");
  }

  private captureVisible(): void {
    const selectors = this.selectors();
    for (const selector of selectors) {
      for (const element of document.querySelectorAll(selector)) {
        this.captureElement(element);
      }
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
    const selectors = this.selectors();
    const elements = new Set<Element>();

    for (const selector of selectors) {
      if (element.matches(selector)) {
        elements.add(element);
      }

      for (const found of element.querySelectorAll(selector)) {
        elements.add(found);
      }
    }

    for (const found of elements) {
      this.captureElement(found);
    }
  }

  private captureElement(element: Element): void {
    if (!(element instanceof HTMLElement) || !this.onEvent) {
      return;
    }

    const text = (element.innerText || element.textContent || "").replace(/\s+/g, " ").trim();
    if (!text) {
      return;
    }

    this.onEvent(new SubtitleEvent(text, Date.now()));
  }

  private selectors(): readonly string[] {
    return [
      ".player-timedtext",
      ".player-timedtext-text-container",
      ".player-timedtext-text-container span",
      '[data-uia="subtitle-text"]',
    ];
  }
}





