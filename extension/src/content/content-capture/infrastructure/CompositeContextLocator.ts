import type { IContextLocator } from "@/content/content-capture/application/IContextLocator";
import type { ContextMatch } from "@/content/content-capture/domain/ContextMatch";
import type { WordMatch } from "@/content/content-capture/domain/WordMatch";

export class CompositeContextLocator implements IContextLocator {
  constructor(private readonly locators: readonly IContextLocator[]) {}

  locate(match: WordMatch): ContextMatch | undefined {
    for (const locator of this.locators) {
      const contextMatch = locator.locate(match);
      if (contextMatch) {
        return contextMatch;
      }
    }

    return undefined;
  }
}





