import type { IHighlightPrefsRepository } from "@/content/popup/application/IHighlightPrefsRepository";
import { highlightStyleIdStorageItem } from "./PopupPrefsStorage";

export class HighlightPrefsRepository implements IHighlightPrefsRepository {
  async getHighlightStyleId(): Promise<string> {
    return highlightStyleIdStorageItem.getValue();
  }
}
