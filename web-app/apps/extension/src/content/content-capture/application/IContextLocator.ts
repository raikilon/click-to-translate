import type { ContextMatch } from "@/content/content-capture/domain/ContextMatch";
import type { WordMatch } from "@/content/content-capture/domain/WordMatch";

export interface IContextLocator {
  locate(match: WordMatch): ContextMatch | undefined;
}





