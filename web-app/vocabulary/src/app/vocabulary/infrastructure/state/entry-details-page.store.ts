import { HttpClient, httpResource } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { UsageOrdering } from '../../domain/usage-ordering';
import { EntryDto } from '../dto/entry.dto';
import { VocabularyMapper } from '../mappers/vocabulary.mapper';

@Injectable()
export class EntryDetailsPageStore {
  private readonly vocabularyBasePath = '/vocabulary';

  readonly entryId = signal(0);
  readonly usagesPageIndex = signal(0);
  readonly usagesPageSize = 10;

  readonly entryResource = httpResource<EntryDto>(() => {
    const entryId = this.entryId();
    if (entryId <= 0) {
      return undefined;
    }

    return { url: `${this.vocabularyBasePath}/entries/${entryId}` };
  });

  readonly languageResource = httpResource<string[]>(
    () => ({ url: '/translate/languages' }),
    { defaultValue: [] }
  );

  readonly entry = computed(() => {
    const value = this.entryResource.value();
    return value ? this.mapper.toEntryModel(value) : undefined;
  });

  readonly languages = computed(() =>
    this.languageResource
      .value()
      .map((value) => value.toUpperCase())
      .filter((value, index, array) => array.indexOf(value) === index)
      .sort((a, b) => a.localeCompare(b))
  );

  readonly sortedUsages = computed(() => {
    const value = this.entry();
    return value ? this.usageOrdering.sortByPriority(value.usages) : [];
  });

  readonly usagesTotalPages = computed(() =>
    Math.ceil(this.sortedUsages().length / this.usagesPageSize)
  );

  readonly pagedUsages = computed(() => {
    const from = this.usagesPageIndex() * this.usagesPageSize;
    return this.sortedUsages().slice(from, from + this.usagesPageSize);
  });

  readonly usagesHasNext = computed(
    () => this.usagesPageIndex() + 1 < this.usagesTotalPages()
  );

  constructor(
    private readonly route: ActivatedRoute,
    private readonly httpClient: HttpClient,
    private readonly mapper: VocabularyMapper,
    private readonly usageOrdering: UsageOrdering
  ) {
    this.entryId.set(this.readEntryId());
  }

  updatePage(page: number): void {
    this.usagesPageIndex.set(page);
  }

  async saveTerm(term: string): Promise<void> {
    await firstValueFrom(
      this.httpClient.patch<void>(
        `${this.vocabularyBasePath}/entries/${this.entryId()}`,
        { term }
      )
    );
    this.entryResource.reload();
  }

  async saveTranslation(language: string, translation: string): Promise<void> {
    await firstValueFrom(
      this.httpClient.patch<void>(
        `${this.vocabularyBasePath}/entries/${this.entryId()}/translation`,
        {
          language: language.toUpperCase(),
          translation
        }
      )
    );
    this.entryResource.reload();
  }

  async deleteEntry(): Promise<void> {
    await firstValueFrom(
      this.httpClient.delete<void>(
        `${this.vocabularyBasePath}/entries/${this.entryId()}`
      )
    );
  }

  async deleteUsage(usageId: number): Promise<void> {
    await firstValueFrom(
      this.httpClient.delete<void>(
        `${this.vocabularyBasePath}/entries/${this.entryId()}/usages/${usageId}`
      )
    );
    this.entryResource.reload();
  }

  async starUsage(usageId: number): Promise<void> {
    await firstValueFrom(
      this.httpClient.patch<void>(
        `${this.vocabularyBasePath}/entries/${this.entryId()}/usages/${usageId}/star`,
        null
      )
    );
    this.entryResource.reload();
  }

  private readEntryId(): number {
    const value = Number(this.route.snapshot.paramMap.get('entryId'));
    if (Number.isFinite(value) && value > 0) {
      return value;
    }

    return 0;
  }
}
