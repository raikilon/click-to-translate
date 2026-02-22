import type { Anchor, SubtitleSnapshot, Trigger } from "@domain";

const ROUTE_CHECK_INTERVAL_MS = 1000;
const SUBTITLE_STALE_MS = 4000;
const CANDIDATE_SELECTOR_LIMIT = 120;
const YOUTUBE_CAPTION_SELECTORS = [
  ".ytp-caption-window-container",
  ".ytp-caption-window-bottom",
  ".ytp-caption-window-top",
  ".caption-window",
  ".captions-text",
  '[class*="ytp-caption"]',
  '[class*="caption"]',
  '[id*="caption"]',
];
const VIDEO_ROOT_SELECTORS = [
  "#movie_player",
  ".html5-video-player",
  "ytd-player",
  "#player",
];

let subtitleObserver: MutationObserver | null = null;
let routeCheckTimerId: number | null = null;
let refreshQueued = false;
let currentHref = "";
let lastSubtitleText = "";
let lastSubtitleRect: DOMRect | null = null;
let lastSubtitleUpdatedAtMs = 0;
let lastFallbackPoint: { x: number; y: number } | null = null;

function normalizeText(value: string): string {
  return value.replace(/\s+/g, " ").trim();
}

function getClassName(element: Element): string {
  const className = (element as HTMLElement).className;
  if (typeof className === "string") {
    return className;
  }

  if (
    className &&
    typeof (className as { baseVal?: unknown }).baseVal === "string"
  ) {
    return (className as { baseVal: string }).baseVal;
  }

  return "";
}

function isYouTubeWatchUrl(url: string): boolean {
  return url.includes("youtube.com/watch") || url.includes("youtu.be/");
}

function anchorFromRect(rect: DOMRect): Anchor {
  return {
    kind: "rect",
    x: Math.round(rect.left),
    y: Math.round(rect.top),
    w: Math.round(rect.width),
    h: Math.round(rect.height),
  };
}

function isElementVisible(element: Element): boolean {
  if (!(element instanceof HTMLElement)) {
    return false;
  }

  const style = window.getComputedStyle(element);
  if (
    style.visibility === "hidden" ||
    style.display === "none" ||
    Number(style.opacity || "1") <= 0
  ) {
    return false;
  }

  const rect = element.getBoundingClientRect();
  if (rect.width < 4 || rect.height < 4) {
    return false;
  }

  if (rect.bottom < 0 || rect.top > window.innerHeight) {
    return false;
  }

  return true;
}

function getElementText(element: Element): string {
  if (element instanceof HTMLElement) {
    const fromInnerText = normalizeText(element.innerText || "");
    if (fromInnerText) {
      return fromInnerText;
    }
  }

  return normalizeText(element.textContent || "");
}

function getVideoRoot(): Element | Document {
  for (const selector of VIDEO_ROOT_SELECTORS) {
    const root = document.querySelector(selector);
    if (root) {
      return root;
    }
  }

  return document;
}

function collectCaptionCandidates(root: Element | Document): Element[] {
  const candidates = new Set<Element>();

  for (const selector of YOUTUBE_CAPTION_SELECTORS) {
    const found = root.querySelectorAll(selector);
    const max = Math.min(found.length, CANDIDATE_SELECTOR_LIMIT);
    for (let index = 0; index < max; index += 1) {
      candidates.add(found[index]);
    }
  }

  return [...candidates];
}

function scoreCandidate(element: Element, text: string): number {
  const rect = element.getBoundingClientRect();
  const classAndId = `${getClassName(element)} ${element.id}`.toLowerCase();
  const centerX = rect.left + rect.width / 2;
  const centerDistance = Math.abs(centerX - window.innerWidth / 2);
  const centerPenalty = Math.min(45, (centerDistance / window.innerWidth) * 100);

  let score = 0;
  if (classAndId.includes("ytp-caption")) {
    score += 120;
  } else if (classAndId.includes("caption")) {
    score += 70;
  }

  if (rect.top > window.innerHeight * 0.38) {
    score += 25;
  }

  if (rect.bottom > window.innerHeight * 0.5) {
    score += 15;
  }

  if (text.length >= 2 && text.length <= 190) {
    score += 15;
  } else if (text.length > 320) {
    score -= 35;
  }

  if (rect.width > window.innerWidth * 0.9 && text.length > 220) {
    score -= 25;
  }

  score -= centerPenalty;

  return score;
}

function findBestCaptionCandidate(): { text: string; rect: DOMRect } | null {
  const root = getVideoRoot();
  const candidates = collectCaptionCandidates(root);
  let best: { text: string; rect: DOMRect; score: number } | null = null;

  for (const candidate of candidates) {
    if (!isElementVisible(candidate)) {
      continue;
    }

    const text = getElementText(candidate);
    if (!text) {
      continue;
    }

    const rect = candidate.getBoundingClientRect();
    const score = scoreCandidate(candidate, text);
    if (!best || score > best.score) {
      best = { text, rect, score };
    }
  }

  if (!best || best.score < 25) {
    return null;
  }

  return {
    text: best.text,
    rect: best.rect,
  };
}

function clearSubtitleCache(): void {
  lastSubtitleText = "";
  lastSubtitleRect = null;
  lastSubtitleUpdatedAtMs = 0;
}

function refreshSubtitleCache(): void {
  const extracted = findBestCaptionCandidate();
  if (!extracted) {
    return;
  }

  const normalized = normalizeText(extracted.text);
  if (!normalized) {
    return;
  }

  lastSubtitleText = normalized;
  lastSubtitleRect = extracted.rect;
  lastSubtitleUpdatedAtMs = Date.now();
}

function queueRefresh(): void {
  if (refreshQueued) {
    return;
  }

  refreshQueued = true;
  window.requestAnimationFrame(() => {
    refreshQueued = false;
    refreshSubtitleCache();
  });
}

function stopRouteChecks(): void {
  if (routeCheckTimerId !== null) {
    window.clearInterval(routeCheckTimerId);
    routeCheckTimerId = null;
  }

  window.removeEventListener("popstate", handleRouteMaybeChanged);
  window.removeEventListener("beforeunload", stopYouTubeSubtitleObserver);
}

function handleRouteMaybeChanged(): void {
  const nextHref = window.location.href;
  if (nextHref === currentHref) {
    return;
  }

  currentHref = nextHref;
  clearSubtitleCache();

  if (!isYouTubeWatchUrl(nextHref)) {
    stopYouTubeSubtitleObserver();
    return;
  }

  if (!subtitleObserver) {
    startYouTubeSubtitleObserver();
    return;
  }

  queueRefresh();
}

function startRouteChecks(): void {
  if (routeCheckTimerId !== null) {
    return;
  }

  currentHref = window.location.href;
  routeCheckTimerId = window.setInterval(
    handleRouteMaybeChanged,
    ROUTE_CHECK_INTERVAL_MS,
  );
  window.addEventListener("popstate", handleRouteMaybeChanged);
  window.addEventListener("beforeunload", stopYouTubeSubtitleObserver);
}

export function startYouTubeSubtitleObserver(): void {
  if (subtitleObserver) {
    return;
  }

  if (!isYouTubeWatchUrl(window.location.href)) {
    return;
  }

  const observationRoot = document.body ?? document.documentElement;
  if (!observationRoot) {
    return;
  }

  subtitleObserver = new MutationObserver(() => {
    queueRefresh();
  });
  subtitleObserver.observe(observationRoot, {
    subtree: true,
    childList: true,
    characterData: true,
  });

  startRouteChecks();
  refreshSubtitleCache();
}

export function stopYouTubeSubtitleObserver(): void {
  if (subtitleObserver) {
    subtitleObserver.disconnect();
    subtitleObserver = null;
  }

  stopRouteChecks();
  clearSubtitleCache();
}

function isSubtitleFresh(): boolean {
  return Date.now() - lastSubtitleUpdatedAtMs <= SUBTITLE_STALE_MS;
}

function fallbackAnchorFromPoint(): Anchor | null {
  if (!lastFallbackPoint) {
    return null;
  }

  return {
    kind: "point",
    x: lastFallbackPoint.x,
    y: lastFallbackPoint.y,
  };
}

export async function getSubtitleSnapshot(
  trigger: Trigger,
): Promise<SubtitleSnapshot | null> {
  const url = trigger.url || window.location.href;
  if (!isYouTubeWatchUrl(url)) {
    stopYouTubeSubtitleObserver();
    return null;
  }

  lastFallbackPoint = {
    x: trigger.mouse.x,
    y: trigger.mouse.y,
  };

  startYouTubeSubtitleObserver();
  refreshSubtitleCache();

  if (!lastSubtitleText || !isSubtitleFresh()) {
    return null;
  }

  const anchor =
    (lastSubtitleRect ? anchorFromRect(lastSubtitleRect) : null) ??
    fallbackAnchorFromPoint();

  if (!anchor) {
    return null;
  }

  return {
    text: lastSubtitleText,
    provider: "youtube",
    anchor,
  };
}
