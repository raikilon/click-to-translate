import type { IWordLocator } from "@/content/content-capture/application/IWordLocator";
import { Anchor } from "@/content/content-capture/domain/Anchor";
import type { CapturePoint } from "@/content/content-capture/domain/CapturePoint";
import { WordMatch } from "@/content/content-capture/domain/WordMatch";
import { WordBoundaryFinder } from "@/content/content-capture/domain/WordBoundaryFinder";

const NETFLIX_SUBTITLE_SELECTOR = ".player-timedtext .player-timedtext-text-container";

const WORD_REGEX = /[0-9A-Za-z\u00C0-\u024F']+/gu;
const MAX_POINTER_DISTANCE_PX = 220;

export class NetflixSubtitleWordLocator implements IWordLocator {
  constructor(private readonly wordBoundaryFinder: WordBoundaryFinder) {}

  locate(point: CapturePoint): WordMatch | undefined {
    if (!window.location.href.includes("netflix.com/watch")) {
      return undefined;
    }

    const subtitleRoots = this.collectSubtitleRoots();
    if (!subtitleRoots.length) {
      return undefined;
    }

    let best:
      | {
          match: WordMatch;
          distance: number;
        }
      | undefined;

    for (const root of subtitleRoots) {
      const textNodes = this.collectTextNodes(root);
      for (const textNode of textNodes) {
        const candidate = this.locateInTextNode(textNode, point);
        if (!candidate) {
          continue;
        }

        if (!best || candidate.distance < best.distance) {
          best = candidate;
        }
      }
    }

    if (!best || best.distance > MAX_POINTER_DISTANCE_PX) {
      return undefined;
    }

    return best.match;
  }

  private collectSubtitleRoots(): Element[] {
    const roots: Element[] = [];
    for (const element of document.querySelectorAll(NETFLIX_SUBTITLE_SELECTOR)) {
      if (!(element instanceof HTMLElement)) {
        continue;
      }

      if (!this.isVisible(element)) {
        continue;
      }

      const text = (element.innerText || element.textContent || "").replace(/\s+/g, " ").trim();
      if (!text) {
        continue;
      }

      roots.push(element);
    }

    return roots;
  }

  private collectTextNodes(root: Element): Text[] {
    const nodes: Text[] = [];
    const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT);
    let current = walker.nextNode();
    while (current) {
      if (current.nodeType === Node.TEXT_NODE) {
        const text = current.nodeValue?.trim();
        if (text) {
          nodes.push(current as Text);
        }
      }

      current = walker.nextNode();
    }

    return nodes;
  }

  private locateInTextNode(
    textNode: Text,
    point: CapturePoint,
  ): { match: WordMatch; distance: number } | undefined {
    const sourceText = textNode.nodeValue ?? "";
    if (!sourceText) {
      return undefined;
    }

    let best:
      | {
          match: WordMatch;
          distance: number;
        }
      | undefined;

    WORD_REGEX.lastIndex = 0;
    let token = WORD_REGEX.exec(sourceText);
    while (token) {
      const rawStart = token.index;
      const rawWord = token[0];
      const rawEnd = rawStart + rawWord.length;
      const boundary = this.wordBoundaryFinder.find(sourceText, rawStart);
      if (boundary && boundary.start <= rawStart && boundary.end >= rawEnd) {
        const rect = this.measureRect(textNode, boundary.start, boundary.end);
        if (rect) {
          const distance = this.distanceToRect(rect, point);
          const match = new WordMatch(
            boundary.word,
            textNode,
            sourceText,
            boundary.start,
            boundary.end,
            new Anchor(
              Math.round(rect.left + rect.width / 2),
              Math.round(rect.bottom),
            ),
          );

          if (!best || distance < best.distance) {
            best = { match, distance };
          }
        }
      }

      token = WORD_REGEX.exec(sourceText);
    }

    return best;
  }

  private measureRect(textNode: Text, start: number, end: number): DOMRect | undefined {
    if (start < 0 || end <= start) {
      return undefined;
    }

    const length = textNode.length;
    if (start > length || end > length) {
      return undefined;
    }

    const range = document.createRange();
    range.setStart(textNode, start);
    range.setEnd(textNode, end);
    const rects = range.getClientRects();
    if (!rects.length) {
      return undefined;
    }

    return rects[0];
  }

  private distanceToRect(rect: DOMRect, point: CapturePoint): number {
    const dx =
      point.x < rect.left ? rect.left - point.x : point.x > rect.right ? point.x - rect.right : 0;
    const dy =
      point.y < rect.top ? rect.top - point.y : point.y > rect.bottom ? point.y - rect.bottom : 0;
    return Math.hypot(dx, dy);
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
