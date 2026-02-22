import type { Anchor, SubtitleSnapshot, Trigger } from "@domain";

const ROUTE_CHECK_INTERVAL_MS = 1000;
const SUBTITLE_STALE_MS = 9000;
const CANDIDATE_SELECTOR_LIMIT = 160;
const FALLBACK_SCAN_LIMIT = 2200;
const NETFLIX_SUBTITLE_SELECTORS = [
  '[class*="player-timedtext"]',
  '[class*="timedtext"]',
  '[class*="subtitle"]',
  '[class*="subtitles"]',
  '[class*="caption"]',
  '[id*="timedtext"]',
  '[id*="subtitle"]',
  '[data-uia*="timedtext"]',
  '[data-uia*="subtitle"]',
  '[aria-live]',
];
const VIDEO_ROOT_SELECTORS = [
  '[data-uia="watch-video"]',
  ".watch-video",
  ".watch-video--player-view",
  ".nf-player-container",
];

let subtitleObserver: MutationObserver | null = null;
let routeCheckTimerId: number | null = null;
let refreshQueued = false;
let currentHref = "";
let lastSubtitleText: string | null = null;
let lastAnchor: Anchor | null = null;
let lastSubtitleUpdatedAtMs = 0;
let lastFallbackPoint: { x: number; y: number } | null = null;

type CandidateHistory = {
  lastText: string;
  updates: number;
};

const candidateHistory = new WeakMap<Element, CandidateHistory>();

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

function isNetflixWatchUrl(url: string): boolean {
  return url.includes("netflix.com/watch");
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

function anchorFromRect(rect: DOMRect): Anchor {
  return {
    kind: "rect",
    x: Math.round(rect.left),
    y: Math.round(rect.top),
    w: Math.round(rect.width),
    h: Math.round(rect.height),
  };
}

function trackCandidateUpdates(element: Element, text: string): number {
  const previous = candidateHistory.get(element);
  if (!previous) {
    candidateHistory.set(element, {
      lastText: text,
      updates: 0,
    });
    return 0;
  }

  if (previous.lastText === text) {
    return previous.updates;
  }

  const updates = Math.min(previous.updates + 1, 6);
  candidateHistory.set(element, {
    lastText: text,
    updates,
  });
  return updates;
}

function countLines(element: Element, text: string): number {
  if (element instanceof HTMLElement) {
    const raw = element.innerText || "";
    if (raw.includes("\n")) {
      const lines = raw
        .split("\n")
        .map((line) => normalizeText(line))
        .filter(Boolean);
      if (lines.length > 0) {
        return lines.length;
      }
    }
  }

  if (!text) {
    return 0;
  }

  return Math.max(1, Math.ceil(text.length / 42));
}

function scoreCandidate(
  element: Element,
  text: string,
  extraClassScore: number,
): number {
  const rect = element.getBoundingClientRect();
  const classAndId = `${getClassName(element)} ${element.id}`.toLowerCase();
  const lineCount = countLines(element, text);
  const centerX = rect.left + rect.width / 2;
  const centerDistance = Math.abs(centerX - window.innerWidth / 2);
  const centerPenalty = Math.min(
    55,
    (centerDistance / Math.max(window.innerWidth, 1)) * 120,
  );
  const updateBonus = trackCandidateUpdates(element, text) * 6;

  let score = extraClassScore;

  if (classAndId.includes("player-timedtext")) {
    score += 120;
  } else if (classAndId.includes("timedtext")) {
    score += 90;
  } else if (classAndId.includes("subtitle")) {
    score += 70;
  } else if (classAndId.includes("caption")) {
    score += 50;
  }

  if (rect.top >= window.innerHeight * 0.58) {
    score += 30;
  }

  if (rect.bottom >= window.innerHeight * 0.68) {
    score += 12;
  }

  if (text.length >= 2 && text.length <= 190) {
    score += 18;
  } else if (text.length > 240) {
    score -= 40;
  }

  if (lineCount <= 2) {
    score += 18;
  } else if (lineCount === 3) {
    score += 6;
  } else {
    score -= 26;
  }

  if (rect.width < window.innerWidth * 0.12 || rect.height < 8) {
    score -= 20;
  }

  if (rect.top <= window.innerHeight * 0.35) {
    score -= 16;
  }

  score += updateBonus;
  score -= centerPenalty;

  return score;
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

function collectKnownCandidates(root: Element | Document): Element[] {
  const candidates = new Set<Element>();

  for (const selector of NETFLIX_SUBTITLE_SELECTORS) {
    const found = root.querySelectorAll(selector);
    const max = Math.min(found.length, CANDIDATE_SELECTOR_LIMIT);
    for (let index = 0; index < max; index += 1) {
      candidates.add(found[index]);
    }
  }

  return [...candidates];
}

function collectBottomRegionCandidates(): Element[] {
  const candidates = new Set<Element>();
  const sampleXFractions = [0.18, 0.32, 0.5, 0.68, 0.82];
  const sampleYFractions = [0.62, 0.7, 0.78, 0.86];

  for (const xFraction of sampleXFractions) {
    for (const yFraction of sampleYFractions) {
      const x = Math.round(window.innerWidth * xFraction);
      const y = Math.round(window.innerHeight * yFraction);
      const atPoint = document.elementsFromPoint(x, y).slice(0, 12);

      for (const element of atPoint) {
        candidates.add(element);

        let parent = element.parentElement;
        let depth = 0;
        while (parent && depth < 3) {
          candidates.add(parent);
          parent = parent.parentElement;
          depth += 1;
        }
      }
    }
  }

  const broadCandidates = document.querySelectorAll("div, span, p");
  const max = Math.min(broadCandidates.length, FALLBACK_SCAN_LIMIT);
  for (let index = 0; index < max; index += 1) {
    const element = broadCandidates[index];
    const rect = element.getBoundingClientRect();
    if (rect.top < window.innerHeight * 0.55) {
      continue;
    }

    if (rect.top > window.innerHeight * 1.05) {
      continue;
    }

    candidates.add(element);
  }

  return [...candidates];
}

function pickBestCandidate(
  candidates: Element[],
  baseScore: number,
  minimumScore: number,
): { text: string; rect: DOMRect } | null {
  let best: { text: string; rect: DOMRect; score: number } | null = null;

  for (const candidate of candidates) {
    if (!isElementVisible(candidate)) {
      continue;
    }

    const text = getElementText(candidate);
    if (!text) {
      continue;
    }

    const score = scoreCandidate(candidate, text, baseScore);
    if (!best || score > best.score) {
      best = {
        text,
        rect: candidate.getBoundingClientRect(),
        score,
      };
    }
  }

  if (!best || best.score < minimumScore) {
    return null;
  }

  return {
    text: best.text,
    rect: best.rect,
  };
}

function findBestSubtitleCandidate(): { text: string; rect: DOMRect } | null {
  const root = getVideoRoot();
  const knownCandidates = collectKnownCandidates(root);
  const knownMatch = pickBestCandidate(knownCandidates, 40, 34);
  if (knownMatch) {
    return knownMatch;
  }

  const bottomRegionCandidates = collectBottomRegionCandidates();
  return pickBestCandidate(bottomRegionCandidates, 0, 28);
}

function clearSubtitleCache(): void {
  lastSubtitleText = null;
  lastAnchor = null;
  lastSubtitleUpdatedAtMs = 0;
}

function refreshSubtitleCache(): void {
  const extracted = findBestSubtitleCandidate();
  if (!extracted) {
    return;
  }

  const text = normalizeText(extracted.text);
  if (!text) {
    return;
  }

  lastSubtitleText = text;
  lastAnchor = anchorFromRect(extracted.rect);
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
  window.removeEventListener("beforeunload", stopNetflixSubtitleObserver);
}

function handleRouteMaybeChanged(): void {
  const nextHref = window.location.href;
  if (nextHref === currentHref) {
    return;
  }

  currentHref = nextHref;
  clearSubtitleCache();

  if (!isNetflixWatchUrl(nextHref)) {
    stopNetflixSubtitleObserver();
    return;
  }

  if (!subtitleObserver) {
    startNetflixSubtitleObserver();
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
  window.addEventListener("beforeunload", stopNetflixSubtitleObserver);
}

export function startNetflixSubtitleObserver(): void {
  if (subtitleObserver) {
    return;
  }

  if (!isNetflixWatchUrl(window.location.href)) {
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

export function stopNetflixSubtitleObserver(): void {
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
  if (!isNetflixWatchUrl(url)) {
    stopNetflixSubtitleObserver();
    return null;
  }

  lastFallbackPoint = {
    x: trigger.mouse.x,
    y: trigger.mouse.y,
  };

  startNetflixSubtitleObserver();
  refreshSubtitleCache();

  if (!lastSubtitleText || !isSubtitleFresh()) {
    return null;
  }

  const anchor = lastAnchor ?? fallbackAnchorFromPoint();
  if (!anchor) {
    return null;
  }

  return {
    text: lastSubtitleText,
    provider: "netflix",
    anchor,
  };
}
