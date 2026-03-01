import { Anchor } from "@/content/content-capture/domain/Anchor";
import type { IWordLocator } from "@/content/content-capture/application/IWordLocator";
import type { CapturePoint } from "@/content/content-capture/domain/CapturePoint";
import { WordBoundaryFinder } from "@/content/content-capture/domain/WordBoundaryFinder";
import { WordMatch } from "@/content/content-capture/domain/WordMatch";
import { CaretFromPointResolver } from "./CaretFromPointResolver";
import { RangeRectMeasurer } from "./RangeRectMeasurer";

export class DomWordLocator implements IWordLocator {
  constructor(
    private readonly caretResolver: CaretFromPointResolver,
    private readonly boundaryFinder: WordBoundaryFinder,
    private readonly rangeRectMeasurer: RangeRectMeasurer,
  ) {}

  locate(point: CapturePoint): WordMatch | undefined {
    const caretPoint = this.caretResolver.resolve(point.x, point.y);
    if (!caretPoint) {
      return undefined;
    }

    const sourceText = caretPoint.node.nodeValue;
    if (!sourceText) {
      return undefined;
    }

    const boundary = this.boundaryFinder.find(sourceText, caretPoint.offset);
    if (!boundary) {
      return undefined;
    }

    const rect = this.rangeRectMeasurer.measure(caretPoint.node, boundary.start, boundary.end);
    const anchor = this.toAnchor(point, rect);

    return new WordMatch(
      boundary.word,
      caretPoint.node,
      sourceText,
      boundary.start,
      boundary.end,
      anchor,
    );
  }

  private toAnchor(point: CapturePoint, rect: DOMRect | undefined): Anchor {
    if (!rect) {
      return new Anchor(point.x, point.y);
    }

    const centerX = Math.round(rect.left + rect.width / 2);
    const bottomY = Math.round(rect.bottom);
    return new Anchor(centerX, bottomY);
  }
}





