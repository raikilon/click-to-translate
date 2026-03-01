export interface IHighlightPrefsRepository {
  getHighlightStyleId(): Promise<string>;
  saveHighlightStyleId(styleId: string): Promise<void>;
}
