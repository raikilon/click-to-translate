import type { IContextLocator } from "./IContextLocator";
import { CapturedContext } from "@/content/content-capture/domain/CapturedContext";
import type { CapturedWord } from "@/content/content-capture/domain/CapturedWord";
import { Anchor } from "@/content/content-capture/domain/Anchor";
import { WordMatch } from "@/content/content-capture/domain/WordMatch";

export class CaptureContextForWordUseCase {
  constructor(private readonly contextLocator: IContextLocator) {}

  execute(capturedWord: CapturedWord): CapturedContext | undefined {
    const match = new WordMatch(
      capturedWord.word,
      capturedWord.textNode,
      capturedWord.textNode.nodeValue ?? "",
      capturedWord.start,
      capturedWord.end,
      new Anchor(capturedWord.anchorX, capturedWord.anchorY),
    );
    const contextMatch = this.contextLocator.locate(match);
    if (!contextMatch) {
      return undefined;
    }

    return new CapturedContext(
      contextMatch.word,
      contextMatch.context,
      contextMatch.anchor.x,
      contextMatch.anchor.y,
    );
  }
}
