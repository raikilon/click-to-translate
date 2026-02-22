import type { Anchor } from "../../input/Anchor";
import type { Trigger } from "../../input/Trigger";
import type { CaptureResult } from "../../model/CaptureResult";
import type { DisplayInstruction } from "../../model/DisplayInstruction";
import type { Snapshots, Strategy } from "../Strategy";

function normalizeText(value: string): string {
  return value.replace(/\s+/g, " ").trim();
}

function getPointAnchor(trigger: Trigger): Anchor {
  return {
    kind: "point",
    x: trigger.mouse.x,
    y: trigger.mouse.y,
  };
}

function getWord(snapshots: Snapshots): string | null {
  const fromSelection = snapshots.selection?.selectedText
    ? normalizeText(snapshots.selection.selectedText)
    : "";

  if (fromSelection) {
    return fromSelection;
  }

  const fromTextAtPoint = snapshots.textAtPoint?.word
    ? normalizeText(snapshots.textAtPoint.word)
    : "";

  return fromTextAtPoint || null;
}

function buildSentenceFromContext(word: string, surroundingText?: string): string | null {
  if (!surroundingText) {
    return null;
  }

  const normalized = normalizeText(surroundingText);
  if (!normalized) {
    return null;
  }

  const wordIndex = normalized.toLowerCase().indexOf(word.toLowerCase());
  if (wordIndex < 0) {
    return normalized;
  }

  const start = Math.max(0, wordIndex - 80);
  const end = Math.min(normalized.length, wordIndex + word.length + 80);
  return normalized.slice(start, end).trim();
}

export class NetflixStrategy implements Strategy {
  readonly id = "netflix" as const;

  matches(url: string): boolean {
    return url.includes("netflix.com/watch");
  }

  computeCapture(trigger: Trigger, snapshots: Snapshots): CaptureResult | null {
    const word = getWord(snapshots);
    if (!word) {
      return null;
    }

    const netflixSubtitle =
      snapshots.subtitle?.provider === "netflix" &&
      normalizeText(snapshots.subtitle.text)
        ? snapshots.subtitle
        : null;

    if (netflixSubtitle) {
      const anchor =
        netflixSubtitle.anchor ??
        snapshots.selection?.anchor ??
        snapshots.textAtPoint?.anchor ??
        getPointAnchor(trigger);

      return {
        word,
        sentence: normalizeText(netflixSubtitle.text),
        source: "NETFLIX",
        anchor,
      };
    }

    const fallbackAnchor =
      snapshots.selection?.anchor ??
      snapshots.textAtPoint?.anchor ??
      getPointAnchor(trigger);
    const fallbackSentence =
      buildSentenceFromContext(word, snapshots.textAtPoint?.surroundingText) ?? word;

    return {
      word,
      sentence: fallbackSentence,
      source: "WEB_PAGE",
      anchor: fallbackAnchor,
    };
  }

  computeDisplay(
    capture: CaptureResult,
    _trigger: Trigger,
    snapshots: Snapshots,
  ): DisplayInstruction {
    if (snapshots.subtitle?.provider === "netflix") {
      return {
        mode: "VIDEO_OVERLAY",
        anchor: snapshots.subtitle.anchor,
      };
    }

    return {
      mode: "TOOLTIP",
      anchor: capture.anchor,
      dismissOn: {
        outsideClick: true,
        escape: true,
      },
    };
  }
}
