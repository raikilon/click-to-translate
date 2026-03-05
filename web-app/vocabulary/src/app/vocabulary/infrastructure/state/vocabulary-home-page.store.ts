import {
  HttpClient,
  HttpResourceRequest,
  httpResource
} from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { ParsedSearchQuery } from '../../domain/search-query.model';
import { EntryDto } from '../dto/entry.dto';
import { PageEnvelopeDto } from '../dto/page-envelope.dto';
import { VocabularyMapper } from '../mappers/vocabulary.mapper';

@Injectable()
export class VocabularyHomePageStore {
  private readonly vocabularyBasePath = '/api/vocabulary';

  readonly searchQuery = signal('');
  readonly page = signal(0);
  readonly pageSize = 12;

  private readonly search = signal<ParsedSearchQuery>({ mode: 'all', query: '' });
  private readonly languageToken = signal<string | null>(null);

  readonly languageResource = httpResource<string[]>(
    () => ({ url: '/api/translate/languages' }),
    { defaultValue: [] }
  );

  readonly entriesResource = httpResource<PageEnvelopeDto<EntryDto>>(
    () => this.buildEntriesRequest(),
    {
      defaultValue: {
        items: [],
        page: 0,
        size: this.pageSize,
        totalItems: 0,
        totalPages: 0,
        hasNext: false
      }
    }
  );

  readonly entriesPage = computed(() =>
    this.mapper.toEntryPageModel(this.entriesResource.value())
  );

  readonly languages = computed(() =>
    this.languageResource
      .value()
      .map((value) => value.toUpperCase())
      .filter((value, index, array) => array.indexOf(value) === index)
      .sort((a, b) => a.localeCompare(b))
  );

  readonly languageSuggestions = computed(() => {
    const token = this.languageToken();
    if (token === null) {
      return [];
    }

    if (!token) {
      return this.languages();
    }

    return this.languages().filter((language) => language.startsWith(token));
  });

  constructor(
    private readonly httpClient: HttpClient,
    private readonly mapper: VocabularyMapper
  ) {}

  updateSearch(query: string, parsed: ParsedSearchQuery, token: string | null): void {
    this.searchQuery.set(query);
    this.search.set(parsed);
    this.languageToken.set(token);
    this.page.set(0);
  }

  updatePage(page: number): void {
    this.page.set(page);
  }

  async deleteEntry(entryId: number): Promise<void> {
    await firstValueFrom(
      this.httpClient.delete<void>(`${this.vocabularyBasePath}/entries/${entryId}`)
    );
    this.entriesResource.reload();
  }

  private buildEntriesRequest(): HttpResourceRequest {
    const search = this.search();

    if (search.mode === 'language' && search.language) {
      return {
        url: `${this.vocabularyBasePath}/${search.language}`,
        params: this.baseParams()
      };
    }

    if (search.mode === 'query') {
      return {
        url: `${this.vocabularyBasePath}/search`,
        params: {
          ...this.baseParams(),
          q: search.query
        }
      };
    }

    return {
      url: this.vocabularyBasePath,
      params: this.baseParams()
    };
  }

  private baseParams(): { page: number; size: number; sort: readonly string[] } {
    return {
      page: this.page(),
      size: this.pageSize,
      sort: ['createdAt,desc']
    };
  }
}
