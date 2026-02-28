export class WordAtOffsetExtractor {
  extract(text: string, offset: number): string | undefined {
    const wordCharacterIndex = this.resolveWordCharacterIndex(text, offset);
    if (wordCharacterIndex === undefined) {
      return undefined;
    }

    const wordStartIndex = this.findWordStartIndex(text, wordCharacterIndex);
    const wordEndIndex = this.findWordEndIndex(text, wordCharacterIndex);
    const word = text.slice(wordStartIndex, wordEndIndex).trim();
    return word || undefined;
  }

  private resolveWordCharacterIndex(text: string, offset: number): number | undefined {
    const clampedOffset = Math.max(0, Math.min(offset, text.length));
    const currentIndex = clampedOffset === text.length ? clampedOffset - 1 : clampedOffset;
    if (this.isWordCharacterIndex(text, currentIndex)) {
      return currentIndex;
    }

    const previousIndex = currentIndex - 1;
    if (this.isWordCharacterIndex(text, previousIndex)) {
      return previousIndex;
    }

    return undefined;
  }

  private isWordCharacterIndex(text: string, index: number): boolean {
    if (index < 0 || index >= text.length) {
      return false;
    }

    return this.isWordChar(text[index]);
  }

  private findWordStartIndex(text: string, wordCharacterIndex: number): number {
    let startIndex = wordCharacterIndex;
    while (startIndex > 0 && this.isWordChar(text[startIndex - 1])) {
      startIndex -= 1;
    }

    return startIndex;
  }

  private findWordEndIndex(text: string, wordCharacterIndex: number): number {
    let endIndex = wordCharacterIndex + 1;
    while (endIndex < text.length && this.isWordChar(text[endIndex])) {
      endIndex += 1;
    }

    return endIndex;
  }

  private isWordChar(value: string): boolean {
    return /[0-9A-Za-z\u00C0-\u024F']/u.test(value);
  }
}
