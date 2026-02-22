import type { SubtitleSnapshot, Trigger } from "@domain";

export interface PointOffset {
  x: number;
  y: number;
}

export interface PointSubtitleProbeConfig {
  provider: "youtube" | "netflix";
  isWatchUrl(url: string): boolean;
  subtitleSelectors: readonly string[];
  videoRootSelectors: readonly string[];
  subtitleHints: readonly string[];
  maxPointElements: number;
  maxAncestorDepth: number;
  selectorCandidateLimit: number;
  pointMarginPx: number;
  nearbyPointOffsets: readonly PointOffset[];
}

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

function getElementText(element: Element): string {
  if (!(element instanceof HTMLElement)) {
    return "";
  }

  const fromInnerText = normalizeText(element.innerText || "");
  if (fromInnerText) {
    return fromInnerText;
  }

  return normalizeText(element.textContent || "");
}

function toRectAnchor(rect: DOMRect): SubtitleSnapshot["anchor"] {
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

function isInVideoRoot(
  element: Element,
  videoRootSelectors: readonly string[],
): boolean {
  for (const selector of videoRootSelectors) {
    if (element.closest(selector)) {
      return true;
    }
  }

  return false;
}

function classOrIdContainsSubtitleHint(
  element: Element,
  subtitleHints: readonly string[],
): boolean {
  const classAndId = `${getClassName(element)} ${element.id}`.toLowerCase();
  for (const hint of subtitleHints) {
    if (classAndId.includes(hint)) {
      return true;
    }
  }

  return false;
}

function isSubtitleCandidateElement(
  element: Element,
  config: PointSubtitleProbeConfig,
): boolean {
  if (!isInVideoRoot(element, config.videoRootSelectors)) {
    return false;
  }

  for (const selector of config.subtitleSelectors) {
    if (element.matches(selector)) {
      return true;
    }
  }

  return classOrIdContainsSubtitleHint(element, config.subtitleHints);
}

function isNearPoint(
  x: number,
  y: number,
  rect: DOMRect,
  pointMarginPx: number,
): boolean {
  return (
    x >= rect.left - pointMarginPx &&
    x <= rect.right + pointMarginPx &&
    y >= rect.top - pointMarginPx &&
    y <= rect.bottom + pointMarginPx
  );
}

function isInsideViewport(x: number, y: number): boolean {
  return x >= 0 && y >= 0 && x <= window.innerWidth && y <= window.innerHeight;
}

function isValidSubtitleElementForPoint(
  element: HTMLElement,
  x: number,
  y: number,
  pointMarginPx: number,
): boolean {
  if (!isElementVisible(element)) {
    return false;
  }

  const text = getElementText(element);
  if (!text) {
    return false;
  }

  const rect = element.getBoundingClientRect();
  return isNearPoint(x, y, rect, pointMarginPx);
}

function findSubtitleAncestor(
  element: Element,
  config: PointSubtitleProbeConfig,
): HTMLElement | null {
  let current: Element | null = element;
  for (
    let depth = 0;
    current && depth <= config.maxAncestorDepth;
    depth += 1
  ) {
    if (
      current instanceof HTMLElement &&
      isSubtitleCandidateElement(current, config)
    ) {
      return current;
    }
    current = current.parentElement;
  }

  return null;
}

function collectSelectorCandidates(
  config: PointSubtitleProbeConfig,
): HTMLElement[] {
  const candidates: HTMLElement[] = [];
  const seen = new Set<Element>();

  for (const selector of config.subtitleSelectors) {
    const found = document.querySelectorAll(selector);
    for (const element of found) {
      if (!(element instanceof HTMLElement) || seen.has(element)) {
        continue;
      }
      seen.add(element);
      candidates.push(element);
      if (candidates.length >= config.selectorCandidateLimit) {
        return candidates;
      }
    }
  }

  return candidates;
}

function pickSubtitleFromPoint(
  x: number,
  y: number,
  config: PointSubtitleProbeConfig,
): HTMLElement | null {
  if (!isInsideViewport(x, y)) {
    return null;
  }

  const pointElements = document.elementsFromPoint(x, y);
  const max = Math.min(pointElements.length, config.maxPointElements);
  for (let index = 0; index < max; index += 1) {
    const ancestor = findSubtitleAncestor(pointElements[index], config);
    if (!ancestor) {
      continue;
    }

    if (isValidSubtitleElementForPoint(ancestor, x, y, config.pointMarginPx)) {
      return ancestor;
    }
  }

  return null;
}

function pickSubtitleFromSelectorFallback(
  x: number,
  y: number,
  config: PointSubtitleProbeConfig,
): HTMLElement | null {
  for (const element of collectSelectorCandidates(config)) {
    if (isValidSubtitleElementForPoint(element, x, y, config.pointMarginPx)) {
      return element;
    }
  }

  return null;
}

function pickSubtitleElementAtPoint(
  x: number,
  y: number,
  config: PointSubtitleProbeConfig,
): HTMLElement | null {
  for (const offset of config.nearbyPointOffsets) {
    const fromPoint = pickSubtitleFromPoint(x + offset.x, y + offset.y, config);
    if (fromPoint) {
      return fromPoint;
    }
  }

  for (const offset of config.nearbyPointOffsets) {
    const fromSelectors = pickSubtitleFromSelectorFallback(
      x + offset.x,
      y + offset.y,
      config,
    );
    if (fromSelectors) {
      return fromSelectors;
    }
  }

  return null;
}

export function getSubtitleSnapshotByPoint(
  trigger: Trigger,
  config: PointSubtitleProbeConfig,
): SubtitleSnapshot | null {
  const url = trigger.url || window.location.href;
  if (!config.isWatchUrl(url)) {
    return null;
  }

  const subtitleElement = pickSubtitleElementAtPoint(
    trigger.mouse.x,
    trigger.mouse.y,
    config,
  );
  if (!subtitleElement) {
    return null;
  }

  const text = getElementText(subtitleElement);
  if (!text) {
    return null;
  }

  return {
    text,
    provider: config.provider,
    anchor: toRectAnchor(subtitleElement.getBoundingClientRect()),
  };
}
