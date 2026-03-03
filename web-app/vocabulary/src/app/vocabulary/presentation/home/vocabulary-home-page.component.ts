import { CommonModule } from '@angular/common';
import { httpResource } from '@angular/common/http';
import { Component, computed, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HighlightStrategyFactory } from '../../application/highlight/highlight-strategy.factory';
import { emptyPageModel } from '../../domain/page.model';
import { VocabularyEntryModel } from '../../domain/vocabulary-entry.model';
import { VocabularyFacade } from '../../application/vocabulary.facade';
import { EntryListComponent } from './entry-list.component';
import { SearchBarComponent } from './search-bar.component';
import { PaginationComponent } from '../shared/pagination.component';

@Component({
  selector: 'app-vocabulary-home-page',
  standalone: true,
  imports: [
    CommonModule,
    SearchBarComponent,
    EntryListComponent,
    PaginationComponent
  ],
  templateUrl: './vocabulary-home-page.component.html'
})
export class VocabularyHomePageComponent {
  protected readonly searchQuery = signal('');
  protected readonly page = signal(0);
  protected readonly pageSize = 12;

  protected readonly languageResource = httpResource(
    () => this.vocabularyFacade.buildLanguagesRequest(),
    {
      defaultValue: [] as string[],
      parse: (payload: unknown) => this.vocabularyFacade.parseLanguages(payload)
    }
  );

  protected readonly entriesResource = httpResource(
    () =>
      this.vocabularyFacade.buildEntriesRequest(
        this.searchQuery(),
        this.page(),
        this.pageSize
      ),
    {
      defaultValue: emptyPageModel<VocabularyEntryModel>(),
      parse: (payload: unknown) => this.vocabularyFacade.parseEntriesPage(payload)
    }
  );

  protected readonly entriesPage = computed(() => this.entriesResource.value());
  protected readonly highlightClassName = computed(() =>
    this.highlightStrategyFactory.currentClassName()
  );
  protected readonly languageSuggestions = computed(() =>
    this.vocabularyFacade.languageSuggestions(
      this.languageResource.value(),
      this.searchQuery()
    )
  );

  constructor(
    private readonly vocabularyFacade: VocabularyFacade,
    private readonly highlightStrategyFactory: HighlightStrategyFactory,
    private readonly router: Router
  ) {}

  updateQuery(value: string): void {
    this.searchQuery.set(value);
    this.page.set(0);
  }

  applyLanguageSuggestion(language: string): void {
    this.searchQuery.set(
      this.vocabularyFacade.applyLanguageSuggestion(this.searchQuery(), language)
    );
    this.page.set(0);
  }

  changePage(page: number): void {
    this.page.set(page);
  }

  async deleteEntry(entryId: number): Promise<void> {
    const confirmed = window.confirm('Delete this vocabulary entry?');
    if (!confirmed) {
      return;
    }

    await this.vocabularyFacade.deleteEntry(entryId);
    this.entriesResource.reload();
  }

  openSettings(): void {
    void this.router.navigate(['/settings']);
  }
}
