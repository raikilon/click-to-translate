import { HttpResourceRequest } from '@angular/common/http';

export interface VocabularyGateway {
  listAllRequest(page: number, size: number): HttpResourceRequest;
  searchRequest(query: string, page: number, size: number): HttpResourceRequest;
  listByLanguageRequest(
    language: string,
    page: number,
    size: number
  ): HttpResourceRequest;
  getEntryRequest(entryId: number): HttpResourceRequest;
  listUsagesRequest(
    entryId: number,
    page: number,
    size: number
  ): HttpResourceRequest;
  listUsagePreviewRequest(entryId: number): HttpResourceRequest;
  updateTerm(entryId: number, term: string): Promise<void>;
  updateTranslation(
    entryId: number,
    language: string,
    translation: string
  ): Promise<void>;
  deleteEntry(entryId: number): Promise<void>;
  deleteUsage(entryId: number, usageId: number): Promise<void>;
  starUsage(entryId: number, usageId: number): Promise<void>;
}
