import { Anchor } from "./Anchor";
import { DomainValidationError } from "./DomainValidationError";
import { WordAtOffsetExtractor } from "./WordAtOffsetExtractor";

export interface CapturedOffsetText {
  text: string;
  offset: number;
}

export interface CapturedClickCandidate {
  selectedText?: string;
  contextTextAround?: string;
  offsetText?: CapturedOffsetText;
  anchor: Anchor;
}

interface CapturedClickSeed {
  selectedWord: string;
  textAround: string;
  anchor: Anchor;
}

export class CapturedClick {
  private static readonly wordAtOffsetExtractor = new WordAtOffsetExtractor();

  constructor(
    readonly selectedWord: string,
    readonly textAround: string,
    readonly anchor: Anchor,
  ) {}

  static hydrate(capturedClick: CapturedClick): CapturedClick {
    return new CapturedClick(
      this.normalizeRequiredSelectedWord(capturedClick.selectedWord),
      this.normalizeRequiredText(capturedClick.textAround),
      new Anchor(capturedClick.anchor.x, capturedClick.anchor.y),
    );
  }

  static fromCandidate(candidate: CapturedClickCandidate): CapturedClick | null {
    const selectedText = this.normalizeOptionalText(candidate.selectedText);
    const textAround = this.resolveTextAround(candidate);

    if (selectedText) {
      return this.tryCreate({
        selectedWord: selectedText,
        textAround: textAround ?? selectedText,
        anchor: candidate.anchor,
      });
    }

    if (candidate.offsetText) {
      return this.tryCreateFromOffsetText({
        offsetText: candidate.offsetText,
        textAround,
        anchor: candidate.anchor,
      });
    }

    return null;
  }

  private static tryCreate(input: CapturedClickSeed): CapturedClick | null {
    try {
      return new CapturedClick(
        this.normalizeRequiredSelectedWord(input.selectedWord),
        this.normalizeRequiredText(input.textAround),
        input.anchor,
      );
    } catch (error) {
      if (error instanceof DomainValidationError) {
        return null;
      }
      throw error;
    }
  }
  private static tryCreateFromOffsetText(input: {
    offsetText: CapturedOffsetText;
    textAround?: string;
    anchor: Anchor;
  }): CapturedClick | null {
    const offsetSourceText = this.normalizeOptionalText(input.offsetText.text);
    if (!offsetSourceText) {
      return null;
    }

    const selectedWord = this.wordAtOffsetExtractor.extract(
      offsetSourceText,
      input.offsetText.offset,
    );
    if (!selectedWord) {
      return null;
    }

    return this.tryCreate({
      selectedWord,
      textAround: input.textAround ?? offsetSourceText,
      anchor: input.anchor,
    });
  }

  private static resolveTextAround(candidate: CapturedClickCandidate): string | undefined {
    return (
      this.normalizeOptionalText(candidate.contextTextAround) ??
      this.normalizeOptionalText(candidate.offsetText?.text) ??
      this.normalizeOptionalText(candidate.selectedText)
    );
  }

  private static normalizeOptionalText(value: string | undefined | null): string | undefined {
    const normalized = (value ?? "").replace(/\s+/g, " ").trim();
    return normalized || undefined;
  }

  private static normalizeRequiredText(value: string): string {
    const normalized = this.normalizeOptionalText(value);
    if (!normalized) {
      throw new DomainValidationError("textAround must be non-empty.");
    }

    return normalized;
  }

  private static normalizeRequiredSelectedWord(value: string): string {
    const normalized = this.normalizeOptionalText(value);
    if (!normalized) {
      throw new DomainValidationError("selectedWord must be non-empty.");
    }

    return normalized;
  }
}
