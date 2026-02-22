import type { TextAtPoint, Trigger } from "@domain";

interface CaretPoint {
  node: Node;
  offset: number;
}

function isWordChar(value: string): boolean {
  return /[0-9A-Za-z\u00C0-\u024F']/u.test(value);
}

function caretPointFromDocument(x: number, y: number): CaretPoint | null {
  if (typeof document.caretRangeFromPoint === "function") {
    const range = document.caretRangeFromPoint(x, y);
    if (range) {
      return {
        node: range.startContainer,
        offset: range.startOffset,
      };
    }
  }

  if (typeof document.caretPositionFromPoint === "function") {
    const position = document.caretPositionFromPoint(x, y);
    if (position) {
      return {
        node: position.offsetNode,
        offset: position.offset,
      };
    }
  }

  return null;
}

function extractWordAtOffset(text: string, offset: number): string | null {
  if (!text) {
    return null;
  }

  const boundedOffset = Math.max(0, Math.min(offset, text.length));
  let probe = boundedOffset;

  if (probe === text.length) {
    probe -= 1;
  }

  if (probe < 0 || !isWordChar(text[probe])) {
    const leftProbe = probe - 1;
    if (leftProbe < 0 || !isWordChar(text[leftProbe])) {
      return null;
    }
    probe = leftProbe;
  }

  let start = probe;
  while (start > 0 && isWordChar(text[start - 1])) {
    start -= 1;
  }

  let end = probe + 1;
  while (end < text.length && isWordChar(text[end])) {
    end += 1;
  }

  const word = text.slice(start, end).trim();
  return word || null;
}

export function getTextAtPoint(trigger: Trigger): TextAtPoint | null {
  const caretPoint = caretPointFromDocument(trigger.mouse.x, trigger.mouse.y);
  if (!caretPoint) {
    return null;
  }

  if (caretPoint.node.nodeType !== Node.TEXT_NODE) {
    return null;
  }

  const textNode = caretPoint.node as Text;
  const nodeText = textNode.nodeValue || "";
  const word = extractWordAtOffset(nodeText, caretPoint.offset);
  if (!word) {
    return null;
  }

  return {
    word,
    surroundingText: nodeText,
    anchor: {
      kind: "point",
      x: trigger.mouse.x,
      y: trigger.mouse.y,
    },
  };
}
