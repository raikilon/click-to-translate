export interface CaretPoint {
  node: Text;
  offset: number;
}

export class CaretFromPointResolver {
  resolve(x: number, y: number): CaretPoint | undefined {
    const caretPosition = document.caretPositionFromPoint?.(x, y);
    if (caretPosition && caretPosition.offsetNode.nodeType === Node.TEXT_NODE) {
      return {
        node: caretPosition.offsetNode as Text,
        offset: caretPosition.offset,
      };
    }

    const caretRange = document.caretRangeFromPoint?.(x, y);
    if (caretRange && caretRange.startContainer.nodeType === Node.TEXT_NODE) {
      return {
        node: caretRange.startContainer as Text,
        offset: caretRange.startOffset,
      };
    }

    return undefined;
  }
}





