import type { ContextReducer } from "./ContextReducer";

export interface FixedWindowReducerOptions {
  maxChars?: number;
}

const DEFAULT_MAX_CHARS = 160;

export class FixedWindowReducer implements ContextReducer {
  private readonly maxChars: number;

  constructor(options: FixedWindowReducerOptions = {}) {
    this.maxChars = Math.max(20, options.maxChars ?? DEFAULT_MAX_CHARS);
  }

  reduce(input: { selectedWord: string; textAround: string }): string {
    const normalizedText = this.normalizeWhitespace(input.textAround);
    const normalizedWord = this.normalizeWhitespace(input.selectedWord);
    const reduced = this.toWindow(normalizedText, normalizedWord);
    return reduced || normalizedWord;
  }

  private normalizeWhitespace(value: string): string {
    return value.replace(/\s+/g, " ").trim();
  }

  private toWindow(text: string, word: string): string {
    if (text.length <= this.maxChars) {
      return text;
    }

    const lowerText = text.toLowerCase();
    const lowerWord = word.toLowerCase();
    const wordIndex = lowerText.indexOf(lowerWord);

    if (wordIndex < 0) {
      return text.slice(0, this.maxChars).trim();
    }

    const slack = Math.max(0, this.maxChars - word.length);
    let start = Math.max(0, wordIndex - Math.floor(slack / 2));
    let end = start + this.maxChars;

    if (end > text.length) {
      end = text.length;
      start = Math.max(0, end - this.maxChars);
    }

    return text.slice(start, end).trim();
  }
}
