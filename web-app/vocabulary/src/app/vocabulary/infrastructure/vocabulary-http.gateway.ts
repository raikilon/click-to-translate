import { HttpClient, HttpResourceRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { VocabularyGateway } from '../application/vocabulary-gateway';

@Injectable({ providedIn: 'root' })
export class VocabularyHttpGateway implements VocabularyGateway {
  private readonly basePath = '/api/vocabulary';

  constructor(private readonly httpClient: HttpClient) {}

  listAllRequest(page: number, size: number): HttpResourceRequest {
    return {
      url: this.basePath,
      params: this.baseParams(page, size)
    };
  }

  searchRequest(query: string, page: number, size: number): HttpResourceRequest {
    return {
      url: `${this.basePath}/search`,
      params: {
        ...this.baseParams(page, size),
        q: query
      }
    };
  }

  listByLanguageRequest(
    language: string,
    page: number,
    size: number
  ): HttpResourceRequest {
    return {
      url: `${this.basePath}/${encodeURIComponent(language.toUpperCase())}`,
      params: this.baseParams(page, size)
    };
  }

  getEntryRequest(entryId: number): HttpResourceRequest {
    return {
      url: `${this.basePath}/entries/${entryId}`
    };
  }

  listUsagesRequest(
    entryId: number,
    page: number,
    size: number
  ): HttpResourceRequest {
    return {
      url: `${this.basePath}/entries/${entryId}/usages`,
      params: {
        page,
        size,
        sort: ['starred,desc', 'createdAt,desc']
      }
    };
  }

  listUsagePreviewRequest(entryId: number): HttpResourceRequest {
    return {
      url: `${this.basePath}/entries/${entryId}/usages`,
      params: {
        page: 0,
        size: 3,
        sort: ['starred,desc', 'createdAt,desc']
      }
    };
  }

  async updateTerm(entryId: number, term: string): Promise<void> {
    await firstValueFrom(
      this.httpClient.patch<void>(`${this.basePath}/entries/${entryId}`, {
        term
      })
    );
  }

  async updateTranslation(
    entryId: number,
    language: string,
    translation: string
  ): Promise<void> {
    await firstValueFrom(
      this.httpClient.patch<void>(
        `${this.basePath}/entries/${entryId}/translation`,
        {
          language: language.toUpperCase(),
          translation
        }
      )
    );
  }

  async deleteEntry(entryId: number): Promise<void> {
    await firstValueFrom(
      this.httpClient.delete<void>(`${this.basePath}/entries/${entryId}`)
    );
  }

  async deleteUsage(entryId: number, usageId: number): Promise<void> {
    await firstValueFrom(
      this.httpClient.delete<void>(
        `${this.basePath}/entries/${entryId}/usages/${usageId}`
      )
    );
  }

  async starUsage(entryId: number, usageId: number): Promise<void> {
    await firstValueFrom(
      this.httpClient.patch<void>(
        `${this.basePath}/entries/${entryId}/usages/${usageId}/star`,
        null
      )
    );
  }

  private baseParams(page: number, size: number): {
    page: number;
    size: number;
    sort: readonly string[];
  } {
    return {
      page,
      size,
      sort: ['createdAt,desc']
    };
  }
}
