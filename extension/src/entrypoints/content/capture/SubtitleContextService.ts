import { SubtitleBuffer } from "./SubtitleBuffer";
import {
  resolveSubtitleSiteProfile,
  type SubtitleSiteProfile,
} from "./SubtitleSiteProfiles";

export interface BufferedSubtitleContext {
  textAround: string;
}

export class SubtitleContextService {
  private static readonly URL_REFRESH_DEBOUNCE_MS = 60;
  private static readonly MUTATION_FLUSH_DEBOUNCE_MS = 80;

  private readonly subtitleBuffer: SubtitleBuffer;
  private activeProfile: SubtitleSiteProfile | undefined;
  private observer: MutationObserver | undefined;
  private activeUrl: string | undefined;
  private urlTrackingStarted = false;
  private originalPushState: History["pushState"] | undefined;
  private originalReplaceState: History["replaceState"] | undefined;
  private urlRefreshTimeoutId: number | undefined;
  private mutationFlushTimeoutId: number | undefined;
  private readonly pendingScanNodes = new Set<Node>();
  private readonly onUrlMaybeChanged = (): void => {
    this.scheduleUrlRefresh();
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
    if (this.urlTrackingStarted) {
      return;
    }

    this.urlTrackingStarted = true;
    window.addEventListener("popstate", this.onUrlMaybeChanged);
    window.addEventListener("hashchange", this.onUrlMaybeChanged);
    this.installHistoryHooks();
    this.installNavigationApiHook();
  }

  private scheduleUrlRefresh(): void {
    if (this.urlRefreshTimeoutId !== undefined) {
      window.clearTimeout(this.urlRefreshTimeoutId);
    }

    this.urlRefreshTimeoutId = window.setTimeout(() => {
      this.urlRefreshTimeoutId = undefined;
      this.refreshForCurrentUrl();
    }, SubtitleContextService.URL_REFRESH_DEBOUNCE_MS);
  }

  private installHistoryHooks(): void {
    if (!this.originalPushState) {
      this.originalPushState = history.pushState.bind(history);
      history.pushState = ((...args: Parameters<History["pushState"]>) => {
        this.originalPushState?.(...args);
        this.scheduleUrlRefresh();
      }) as History["pushState"];
    }

    if (!this.originalReplaceState) {
      this.originalReplaceState = history.replaceState.bind(history);
      history.replaceState = ((...args: Parameters<History["replaceState"]>) => {
        this.originalReplaceState?.(...args);
        this.scheduleUrlRefresh();
      }) as History["replaceState"];
    }
  }

  private installNavigationApiHook(): void {
    const navigationApi = (window as unknown as { navigation?: EventTarget }).navigation;
    if (!navigationApi) {
      return;
    }

    navigationApi.addEventListener("navigate", this.onUrlMaybeChanged);
  }

  private startObserving(): void {
    if (!this.activeProfile || !document.body) {
      return;
    }

    this.captureVisibleSubtitles(this.activeProfile);

    this.observer = new MutationObserver((mutationList) => {
      for (const mutation of mutationList) {
        this.enqueueNodeForSubtitleScan(mutation.target);
        for (const addedNode of mutation.addedNodes) {
          this.enqueueNodeForSubtitleScan(addedNode);
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
    this.pendingScanNodes.clear();
    if (this.mutationFlushTimeoutId !== undefined) {
      window.clearTimeout(this.mutationFlushTimeoutId);
      this.mutationFlushTimeoutId = undefined;
    }
  }

  private enqueueNodeForSubtitleScan(node: Node): void {
    this.pendingScanNodes.add(node);
    if (this.mutationFlushTimeoutId !== undefined) {
      return;
    }

    this.mutationFlushTimeoutId = window.setTimeout(() => {
      this.mutationFlushTimeoutId = undefined;
      this.flushPendingSubtitleScans();
    }, SubtitleContextService.MUTATION_FLUSH_DEBOUNCE_MS);
  }

  private flushPendingSubtitleScans(): void {
    const profile = this.activeProfile;
    if (!profile) {
      this.pendingScanNodes.clear();
      return;
    }

    const nodes = Array.from(this.pendingScanNodes);
    this.pendingScanNodes.clear();

    for (const node of nodes) {
      this.captureSubtitleTextFromNode(node, profile);
    }
  }

  private captureVisibleSubtitles(profile: SubtitleSiteProfile): void {
    for (const selector of profile.subtitleSelectors) {
      const elements = document.querySelectorAll(selector);
      for (const element of elements) {
        this.captureSubtitleTextFromElement(element);
      }
    }
  }

  private captureSubtitleTextFromNode(node: Node, profile: SubtitleSiteProfile): void {
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
