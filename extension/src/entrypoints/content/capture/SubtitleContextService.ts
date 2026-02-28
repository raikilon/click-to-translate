import { SubtitleBuffer } from "./SubtitleBuffer";
import {
  resolveSubtitleSiteProfile,
  type SubtitleSiteProfile,
} from "./SubtitleSiteProfiles";

export interface BufferedSubtitleContext {
  textAround: string;
}

export class SubtitleContextService {
  private readonly subtitleBuffer: SubtitleBuffer;
  private urlRefreshIntervalId: number | undefined;
  private activeProfile: SubtitleSiteProfile | undefined;
  private observer: MutationObserver | undefined;
  private activeUrl: string | undefined;
  private readonly onUrlMaybeChanged = (): void => {
    this.refreshForCurrentUrl();
  };

  constructor(subtitleBuffer = new SubtitleBuffer()) {
    this.subtitleBuffer = subtitleBuffer;
  }

  initialize(): void {
    this.startUrlTracking();
    this.refreshForCurrentUrl();
  }

  getBufferedContext(): BufferedSubtitleContext | undefined {
    if (!this.activeProfile) {
      return undefined;
    }

    const textAround = this.subtitleBuffer.getJoinedText();
    if (!textAround) {
      return undefined;
    }

    return {
      textAround,
    };
  }

  private refreshForCurrentUrl(): void {
    const currentUrl = window.location.href;
    if (currentUrl === this.activeUrl) {
      return;
    }

    this.activeUrl = currentUrl;
    const nextProfile = resolveSubtitleSiteProfile(currentUrl);

    if (!nextProfile) {
      this.stopObserving();
      this.activeProfile = undefined;
      this.subtitleBuffer.clear();
      return;
    }

    if (this.activeProfile === nextProfile) {
      this.subtitleBuffer.clear();
      this.captureVisibleSubtitles(nextProfile);
      return;
    }

    this.stopObserving();
    this.subtitleBuffer.clear();
    this.activeProfile = nextProfile;
    this.startObserving();
  }

  private startUrlTracking(): void {
    if (this.urlRefreshIntervalId !== undefined) {
      return;
    }

    window.addEventListener("popstate", this.onUrlMaybeChanged);
    window.addEventListener("hashchange", this.onUrlMaybeChanged);

    // Covers SPA navigations that do not emit browser navigation events.
    this.urlRefreshIntervalId = window.setInterval(() => {
      this.refreshForCurrentUrl();
    }, 500);
  }

  private startObserving(): void {
    if (!this.activeProfile || !document.body) {
      return;
    }

    this.captureVisibleSubtitles(this.activeProfile);

    this.observer = new MutationObserver((mutationList) => {
      for (const mutation of mutationList) {
        this.captureSubtitleTextFromNode(mutation.target);
        for (const addedNode of mutation.addedNodes) {
          this.captureSubtitleTextFromNode(addedNode);
        }
      }
    });

    this.observer.observe(document.body, {
      subtree: true,
      childList: true,
      characterData: true,
    });
  }

  private stopObserving(): void {
    this.observer?.disconnect();
    this.observer = undefined;
  }

  private captureVisibleSubtitles(profile: SubtitleSiteProfile): void {
    for (const selector of profile.subtitleSelectors) {
      const elements = document.querySelectorAll(selector);
      for (const element of elements) {
        this.captureSubtitleTextFromElement(element);
      }
    }
  }

  private captureSubtitleTextFromNode(node: Node): void {
    const profile = this.activeProfile;
    if (!profile) {
      return;
    }

    if (node instanceof Element) {
      this.captureElementAndChildren(node, profile);
      return;
    }

    if (node instanceof Text && node.parentElement) {
      this.captureElementAndChildren(node.parentElement, profile);
    }
  }

  private captureElementAndChildren(element: Element, profile: SubtitleSiteProfile): void {
    const matchedElements = new Set<Element>();

    for (const selector of profile.subtitleSelectors) {
      if (element.matches(selector)) {
        matchedElements.add(element);
      }

      for (const nestedMatch of element.querySelectorAll(selector)) {
        matchedElements.add(nestedMatch);
      }
    }

    for (const matchedElement of matchedElements) {
      this.captureSubtitleTextFromElement(matchedElement);
    }
  }

  private captureSubtitleTextFromElement(element: Element): void {
    if (!(element instanceof HTMLElement) || !this.isVisible(element)) {
      return;
    }

    const text = this.normalizeWhitespace(element.innerText || element.textContent || "");
    if (!text) {
      return;
    }

    this.subtitleBuffer.append(text);
  }

  private isVisible(element: HTMLElement): boolean {
    const style = window.getComputedStyle(element);
    if (style.display === "none" || style.visibility === "hidden") {
      return false;
    }

    const rect = element.getBoundingClientRect();
    return rect.width > 0 && rect.height > 0;
  }

  private normalizeWhitespace(value: string): string {
    return value.replace(/\s+/g, " ").trim();
  }
}
