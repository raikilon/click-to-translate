import { SubtitleEvent } from "@/content/content-capture/domain/SubtitleEvent";
import type { ISubtitleWatcher } from "./ISubtitleWatcher";

export class YouTubeSubtitleWatcher implements ISubtitleWatcher {
  private observer: MutationObserver | undefined;
  private onEvent: ((event: SubtitleEvent) => void) | undefined;

  start(onEvent: (event: SubtitleEvent) => void): void {
    if (!this.matchesUrl(window.location.href) || this.observer) {
      this.onEvent = onEvent;
      return;
    }

    this.onEvent = onEvent;
    this.captureVisible();

    this.observer = new MutationObserver((mutations) => {
      for (const mutation of mutations) {
        this.captureFromNode(mutation.target);
        for (const addedNode of mutation.addedNodes) {
          this.captureFromNode(addedNode);
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
    return url.includes("youtube.com/watch") || url.includes("youtu.be/");
  }

  private captureVisible(): void {
    const selectors = this.selectors();
    for (const selector of selectors) {
      for (const element of document.querySelectorAll(selector)) {
        this.captureElement(element);
      }
    }
  }

  private captureFromNode(node: Node): void {
    if (node instanceof Element) {
      this.captureElementTree(node);
      return;
    }

    if (node instanceof Text && node.parentElement) {
      this.captureElementTree(node.parentElement);
    }
  }

  private captureElementTree(element: Element): void {
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
    if (!(element instanceof HTMLElement) || !this.isVisible(element)) {
      return;
    }

    const text = (element.innerText || element.textContent || "").replace(/\s+/g, " ").trim();
    if (!text || !this.onEvent) {
      return;
    }

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

  private selectors(): readonly string[] {
    return [
      ".ytp-caption-window-container",
      ".ytp-caption-window",
      ".caption-window",
      ".captions-text",
      ".ytp-caption-segment",
      '[class*="ytp-caption"]',
      '[class*="caption-window"]',
      '[class*="captions-text"]',
    ];
  }
}





