export class RangeRectMeasurer {
  measure(node: Text, start: number, end: number): DOMRect | undefined {
    const value = node.nodeValue;
    if (!value) {
      return undefined;
    }

    const safeStart = Math.max(0, Math.min(start, value.length));
    const safeEnd = Math.max(safeStart, Math.min(end, value.length));
    if (safeStart === safeEnd) {
      return undefined;
    }

    const range = document.createRange();
    range.setStart(node, safeStart);
    range.setEnd(node, safeEnd);
    const rect = range.getBoundingClientRect();

    if (!rect.width && !rect.height) {
      return undefined;
    }

    return rect;
  }
}





