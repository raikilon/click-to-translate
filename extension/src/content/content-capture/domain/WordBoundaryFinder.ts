export interface WordBoundary {
  start: number;
  end: number;
  word: string;
}

export class WordBoundaryFinder {
  find(text: string, offset: number): WordBoundary | undefined {
    const wordIndex = this.resolveWordIndex(text, offset);
    if (wordIndex === undefined) {
      return undefined;
    }

    const start = this.findStart(text, wordIndex);
    const end = this.findEnd(text, wordIndex);
    const word = text.slice(start, end).trim();
    if (!word) {
      return undefined;
    }

    return { start, end, word };
  }

  private resolveWordIndex(text: string, offset: number): number | undefined {
    if (!text) {
      return undefined;
    }

    const clamped = Math.max(0, Math.min(offset, text.length));
    const current = clamped === text.length ? clamped - 1 : clamped;
    if (this.isWordIndex(text, current)) {
      return current;
    }

    const previous = current - 1;
    if (this.isWordIndex(text, previous)) {
      return previous;
    }

    return undefined;
  }

  private isWordIndex(text: string, index: number): boolean {
    if (index < 0 || index >= text.length) {
      return false;
    }

    return this.isWordCharacter(text[index]);
  }

  private findStart(text: string, from: number): number {
    let start = from;
    while (start > 0 && this.isWordCharacter(text[start - 1])) {
      start -= 1;
    }

    return start;
  }

  private findEnd(text: string, from: number): number {
    let end = from + 1;
    while (end < text.length && this.isWordCharacter(text[end])) {
      end += 1;
    }

    return end;
  }

  private isWordCharacter(value: string): boolean {
    return /[0-9A-Za-z\u00C0-\u024F']/u.test(value);
  }
}





