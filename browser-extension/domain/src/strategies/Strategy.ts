import type { SelectionSnapshot } from "../input/SelectionSnapshot";
import type { SubtitleSnapshot } from "../input/SubtitleSnapshot";
import type { TextAtPoint } from "../input/TextAtPoint";
import type { Trigger } from "../input/Trigger";
import type { CaptureResult } from "../model/CaptureResult";
import type { DisplayInstruction } from "../model/DisplayInstruction";

export type StrategyId = "generic" | "youtube" | "netflix";

export interface PageInfo {
  url: string;
  title?: string;
  hostname?: string;
  provider?: "youtube" | "netflix" | "unknown";
}

export type Snapshots = {
  pageInfo?: PageInfo;
  selection?: SelectionSnapshot | null;
  textAtPoint?: TextAtPoint | null;
  subtitle?: SubtitleSnapshot | null;
};

export interface Strategy {
  id: StrategyId;
  matches(url: string): boolean;
  computeCapture(trigger: Trigger, snapshots: Snapshots): CaptureResult | null;
  computeDisplay(
    capture: CaptureResult,
    trigger: Trigger,
    snapshots: Snapshots,
  ): DisplayInstruction;
}
