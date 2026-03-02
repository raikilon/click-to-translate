import type { WordMatch } from "@/content/content-capture/domain/WordMatch";
import type { CapturePoint } from "@/content/content-capture/domain/CapturePoint";

export interface IWordLocator {
  locate(point: CapturePoint): WordMatch | undefined;
}





