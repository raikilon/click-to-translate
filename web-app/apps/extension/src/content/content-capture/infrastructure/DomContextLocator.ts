import type { IContextLocator } from "@/content/content-capture/application/IContextLocator";
import { ContextMatch } from "@/content/content-capture/domain/ContextMatch";
import { FixedWindowContextExtractor } from "@/content/content-capture/domain/FixedWindowContextExtractor";
import type { WordMatch } from "@/content/content-capture/domain/WordMatch";

export class DomContextLocator implements IContextLocator {
  constructor(
    private readonly contextExtractor: FixedWindowContextExtractor,
  ) {}

  locate(match: WordMatch): ContextMatch | undefined {
    const container = match.textNode.parentElement;
    const containerText = this.normalize(container?.textContent ?? "");
    if (!containerText) {
      return undefined;
    }

    const context = this.contextExtractor.extract(containerText, match.word);
    if (!context) {
      return undefined;
    }

    return new ContextMatch(match.word, context, match.anchor);
  }

  private normalize(value: string): string {
    return value.replace(/\s+/g, " ").trim();
  }
}





