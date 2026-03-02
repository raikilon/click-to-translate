import type { IWordLocator } from "@/content/content-capture/application/IWordLocator";
import type { CapturePoint } from "@/content/content-capture/domain/CapturePoint";
import type { WordMatch } from "@/content/content-capture/domain/WordMatch";

export class CompositeWordLocator implements IWordLocator {
  constructor(private readonly locators: readonly IWordLocator[]) {}

  locate(point: CapturePoint): WordMatch | undefined {
    for (const locator of this.locators) {
      const match = locator.locate(point);
      if (match) {
        return match;
      }
    }

    return undefined;
  }
}





