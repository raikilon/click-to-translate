import type { IWordLocator } from "./IWordLocator";
import { CapturedWord } from "@/content/content-capture/domain/CapturedWord";
import type { CapturePoint } from "@/content/content-capture/domain/CapturePoint";

export class CaptureWordAtPointUseCase {
  constructor(private readonly wordLocator: IWordLocator) {}

  execute(point: CapturePoint): CapturedWord | undefined {
    const match = this.wordLocator.locate(point);
    if (!match) {
      return undefined;
    }

    return new CapturedWord(
      match.word,
      match.textNode,
      match.start,
      match.end,
      match.anchor.x,
      match.anchor.y,
    );
  }
}
