export type SearchMode = 'all' | 'query' | 'language';

export interface ParsedSearchQuery {
  mode: SearchMode;
  query: string;
  language?: string;
}
