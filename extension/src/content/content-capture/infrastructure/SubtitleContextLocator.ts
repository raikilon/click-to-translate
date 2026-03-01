import type { IContextLocator } from "@/content/content-capture/application/IContextLocator";
import type { ISubtitleBuffer } from "@/content/content-capture/application/ISubtitleBuffer";
import { ContextMatch } from "@/content/content-capture/domain/ContextMatch";
import type { WordMatch } from "@/content/content-capture/domain/WordMatch";

export class SubtitleContextLocator implements IContextLocator {
  constructor(private readonly subtitleBuffer: ISubtitleBuffer) {}

  locate(match: WordMatch): ContextMatch | undefined {
    if (!this.subtitleBuffer.hasRecentEntries()) {
      return undefined;
    }

    const subtitleContext = this.subtitleBuffer.getJoinedText();
    if (!subtitleContext) {
      return undefined;
    }

    return new ContextMatch(match.word, subtitleContext, match.anchor);
  }
}





