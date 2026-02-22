import type {
  SelectionSnapshot,
  SubtitleSnapshot,
  TextAtPoint,
  Trigger,
} from "../../../domain/src";

export interface PageInfo {
  url: string;
  title?: string;
  hostname?: string;
  provider?: "youtube" | "netflix" | "unknown";
}

export interface PageProbe {
  getPageInfo(): Promise<PageInfo>;
  getSelectionSnapshot(trigger: Trigger): Promise<SelectionSnapshot | null>;
  getTextAtPoint(trigger: Trigger): Promise<TextAtPoint | null>;
  getSubtitleSnapshot(trigger: Trigger): Promise<SubtitleSnapshot | null>;
}
