import { Anchor, CapturedClick } from "@domain";
import type { BufferedSubtitleContext } from "./SubtitleContextService";
import type { CapturePointInput, PointCapture } from "./types";

interface CaretPoint {
  node: Node;
  offset: number;
}

interface OffsetText {
  text: string;
  offset: number;
}

export class GenericPointCapture implements PointCapture {
  constructor(
    private readonly getBufferedSubtitleContext: () => BufferedSubtitleContext | undefined = () =>
      undefined,
  ) {}

  capture(input: CapturePointInput): CapturedClick | null {
    const subtitleContext = this.getBufferedSubtitleContext();
    const anchor = this.pointAnchor(input.x, input.y);
    const caretPoint = this.getTextCaretPoint(input.x, input.y);
    const offsetText = this.getOffsetTextFromDom(caretPoint);

    return CapturedClick.fromCandidate({
      selectedText: this.selectedTextFromWindow(),
      contextTextAround: this.getContextTextAroundFromSubtitle(subtitleContext),
      offsetText,
      anchor,
    });
  }

  private tryCaretPointFromDocument(x: number, y: number): CaretPoint | undefined {
    const position = document.caretPositionFromPoint?.(x, y);
    if (position) {
      return {
        node: position.offsetNode,
        offset: position.offset,
      };
    }

    return undefined;
  }

  private getContextTextAroundFromSubtitle(
    subtitleContext: BufferedSubtitleContext | undefined,
  ): string | undefined {
    return subtitleContext?.textAround;
  }

  private getOffsetTextFromDom(caretPoint: CaretPoint | undefined): OffsetText | undefined {
    if (!caretPoint) {
      return undefined;
    }

    const textNode = caretPoint.node as Text;
    if (!textNode.nodeValue) {
      return undefined;
    }

    return {
      text: textNode.nodeValue,
      offset: caretPoint.offset,
    };
  }

  private getTextCaretPoint(x: number, y: number): CaretPoint | undefined {
    const caretPoint = this.tryCaretPointFromDocument(x, y);
    if (!caretPoint || caretPoint.node.nodeType !== Node.TEXT_NODE) {
      return undefined;
    }

    return caretPoint;
  }

  private pointAnchor(x: number, y: number): Anchor {
    return new Anchor(x, y);
  }

  private selectedTextFromWindow(): string | undefined {
    return window.getSelection()?.toString();
  }
}
