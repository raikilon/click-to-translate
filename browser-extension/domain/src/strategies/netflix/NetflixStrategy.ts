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

    const anchor =
      netflixSubtitle?.anchor ??
      snapshots.selection?.anchor ??
      snapshots.textAtPoint?.anchor ??
      getPointAnchor(trigger);

    return {
      word,
      sentence: netflixSubtitle ? normalizeText(netflixSubtitle.text) : word,
      source: "NETFLIX",
      anchor,
    };
  }

  computeDisplay(
    _capture: CaptureResult,
    trigger: Trigger,
    snapshots: Snapshots,
  ): DisplayInstruction {
    const anchor =
      snapshots.subtitle?.provider === "netflix"
        ? snapshots.subtitle.anchor
        : getPointAnchor(trigger);

    return {
      mode: "VIDEO_OVERLAY",
      anchor,
    };
  }
}
