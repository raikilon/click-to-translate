import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { appRouteCommands } from '../../../../routing/route.constants';
import { VocabularyHomePageStore } from '../../../infrastructure/state/vocabulary-home-page.store';
import { EntryListComponent } from '../entry-list/entry-list.component';
import { SearchBarComponent } from '../search-bar/search-bar.component';
import { HighlightStrategyFactory } from '../../highlight/highlight-strategy.factory';
import { PaginationComponent } from '../../shared/pagination.component';
import { SearchQueryParser } from '../search-query-parser';

@Component({
  selector: 'app-vocabulary-home-page',
  standalone: true,
  imports: [
    CommonModule,
    SearchBarComponent,
    EntryListComponent,
    PaginationComponent
  ],
  providers: [VocabularyHomePageStore],
  templateUrl: './vocabulary-home-page.component.html'
})
export class VocabularyHomePageComponent {
  private readonly store = inject(VocabularyHomePageStore);

  protected readonly searchQuery = this.store.searchQuery;
  protected readonly languageResource = this.store.languageResource;
  protected readonly entriesResource = this.store.entriesResource;
  protected readonly entriesPage = this.store.entriesPage;
  protected readonly languageSuggestions = this.store.languageSuggestions;
  protected readonly deletionErrorMessage = signal<string | null>(null);
  protected readonly highlightClassName = computed(() =>
    this.highlightStrategyFactory.currentClassName()
  );

  constructor(
    private readonly router: Router,
    private readonly highlightStrategyFactory: HighlightStrategyFactory,
    private readonly searchQueryParser: SearchQueryParser
  ) {}

  updateQuery(value: string): void {
    this.store.updateSearch(
      value,
      this.searchQueryParser.parse(value),
      this.searchQueryParser.extractLanguageToken(value)
    );
  }

  applyLanguageSuggestion(language: string): void {
    const query = this.searchQueryParser.applyLanguageToken(this.searchQuery(), language);
    this.store.updateSearch(
      query,
      this.searchQueryParser.parse(query),
      this.searchQueryParser.extractLanguageToken(query)
    );
  }

  changePage(page: number): void {
    this.store.updatePage(page);
  }

  async deleteEntry(entryId: number): Promise<void> {
    const confirmed = window.confirm('Delete this vocabulary entry?');
    if (!confirmed) {
      return;
    }

    this.deletionErrorMessage.set(null);

    try {
      await this.store.deleteEntry(entryId);
    } catch (error) {
      this.deletionErrorMessage.set(this.asErrorMessage(error));
    }
  }

  openSettings(): void {
    void this.router.navigate(appRouteCommands.settings());
  }

  private asErrorMessage(error: unknown): string {
    if (error instanceof Error && error.message.trim()) {
      return error.message;
    }

    return 'Delete failed. Please try again.';
  }
}
