import type { Anchor } from "../../input/Anchor";
import type { Trigger } from "../../input/Trigger";
import type { CaptureResult } from "../../model/CaptureResult";
import type { DisplayInstruction } from "../../model/DisplayInstruction";
import type { Snapshots, Strategy } from "../Strategy";

const CONTEXT_WINDOW_CHARS = 80;

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

function getWordFromSelection(snapshots: Snapshots): string | null {
  const selectedText = snapshots.selection?.selectedText;
  if (!selectedText) {
    return null;
  }

  const normalized = normalizeText(selectedText);
  if (!normalized || /[\r\n]/.test(selectedText)) {
    return null;
  }

  return normalized;
}

function getWordFromTextAtPoint(snapshots: Snapshots): string | null {
  const word = snapshots.textAtPoint?.word;
  if (!word) {
    return null;
  }

  const normalized = normalizeText(word);
  return normalized || null;
}

function buildSentenceFromContext(
  word: string,
  surroundingText?: string,
): string | null {
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

  const start = Math.max(0, wordIndex - CONTEXT_WINDOW_CHARS);
  const end = Math.min(
    normalized.length,
    wordIndex + word.length + CONTEXT_WINDOW_CHARS,
  );

  return normalized.slice(start, end).trim();
}

export class GenericStrategy implements Strategy {
  readonly id = "generic" as const;

  matches(_url: string): boolean {
    return true;
  }

  computeCapture(trigger: Trigger, snapshots: Snapshots): CaptureResult | null {
    const word = getWordFromSelection(snapshots) ?? getWordFromTextAtPoint(snapshots);
    if (!word) {
      return null;
    }

    const sentence = buildSentenceFromContext(
      word,
      snapshots.textAtPoint?.surroundingText,
    );
    const anchor =
      snapshots.selection?.anchor ??
      snapshots.textAtPoint?.anchor ??
      getPointAnchor(trigger);

    return {
      word,
      sentence: sentence ?? word,
      source: "WEB_PAGE",
      anchor,
    };
  }

  computeDisplay(
    capture: CaptureResult,
    _trigger: Trigger,
    _snapshots: Snapshots,
  ): DisplayInstruction {
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
