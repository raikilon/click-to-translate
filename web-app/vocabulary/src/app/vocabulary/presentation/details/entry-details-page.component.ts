import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { appRouteCommands } from '../../../routing/route.constants';
import { HighlightSegmentModel } from '../../domain/highlight-segment.model';
import { HighlightSegmenter } from '../../domain/highlight-segmenter';
import { UsageModel } from '../../domain/usage.model';
import { EntryDetailsPageStore } from '../../infrastructure/state/entry-details-page.store';
import { HighlightStrategyFactory } from '../highlight/highlight-strategy.factory';
import { PaginationComponent } from '../shared/pagination.component';

@Component({
  selector: 'app-entry-details-page',
  standalone: true,
  imports: [CommonModule, PaginationComponent],
  providers: [EntryDetailsPageStore],
  templateUrl: './entry-details-page.component.html'
})
export class EntryDetailsPageComponent {
  private readonly store = inject(EntryDetailsPageStore);

  protected readonly entryResource = this.store.entryResource;
  protected readonly entry = this.store.entry;
  protected readonly entryErrorMessage = computed(() => {
    const error = this.entryResource.error();
    if (!error) {
      return null;
    }

    if (error instanceof Error && error.message.trim()) {
      return error.message;
    }

    return 'Failed to load entry.';
  });
  protected readonly languages = this.store.languages;
  protected readonly pagedUsages = this.store.pagedUsages;
  protected readonly usagesPageIndex = this.store.usagesPageIndex;
  protected readonly usagesTotalPages = this.store.usagesTotalPages;
  protected readonly usagesHasNext = this.store.usagesHasNext;
  protected readonly highlightClassName = computed(() =>
    this.highlightStrategyFactory.currentClassName()
  );

  protected readonly termCustomizationDraft = signal('');
  protected readonly translationDrafts = signal<Record<string, string>>({});
  protected readonly selectedLanguage = signal('');
  protected readonly selectedLanguageText = signal('');

  constructor(
    private readonly router: Router,
    private readonly highlightStrategyFactory: HighlightStrategyFactory,
    private readonly highlightSegmenter: HighlightSegmenter
  ) {
    effect(() => {
      const entry = this.entry();
      if (!entry) {
        return;
      }

      this.termCustomizationDraft.set(entry.termCustomization ?? entry.term);

      const nextDrafts: Record<string, string> = {};
      for (const translation of entry.translations) {
        nextDrafts[translation.language] = translation.term;
      }

      this.translationDrafts.set(nextDrafts);
    });
  }

  backToList(): void {
    void this.router.navigate(appRouteCommands.home());
  }

  goToSettings(): void {
    void this.router.navigate(appRouteCommands.settings());
  }

  updateTermCustomizationDraft(event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    this.termCustomizationDraft.set(target.value);
  }

  updateTranslationDraft(language: string, event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    const nextDrafts = { ...this.translationDrafts() };
    nextDrafts[language] = target.value;
    this.translationDrafts.set(nextDrafts);
  }

  setSelectedLanguage(event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLSelectElement)) {
      return;
    }

    this.selectedLanguage.set(target.value);
  }

  setSelectedLanguageText(event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    this.selectedLanguageText.set(target.value);
  }

  async saveTermCustomization(): Promise<void> {
    const term = this.termCustomizationDraft().trim();
    if (!term) {
      return;
    }

    await this.store.saveTerm(term);
  }

  async saveTranslation(language: string): Promise<void> {
    const translation = this.translationDrafts()[language]?.trim();
    if (!translation) {
      return;
    }

    await this.store.saveTranslation(language, translation);
  }

  async saveSelectedTranslation(): Promise<void> {
    const language = this.selectedLanguage().trim();
    const translation = this.selectedLanguageText().trim();
    if (!language || !translation) {
      return;
    }

    await this.store.saveTranslation(language, translation);
    this.selectedLanguageText.set('');
  }

  async deleteEntry(): Promise<void> {
    const confirmed = window.confirm('Delete this entry?');
    if (!confirmed) {
      return;
    }

    await this.store.deleteEntry();
    await this.router.navigate(appRouteCommands.home());
  }

  async deleteUsage(usageId: number): Promise<void> {
    const confirmed = window.confirm('Delete this usage?');
    if (!confirmed) {
      return;
    }

    await this.store.deleteUsage(usageId);
  }

  async starUsage(usageId: number): Promise<void> {
    await this.store.starUsage(usageId);
  }

  changeUsagesPage(page: number): void {
    this.store.updatePage(page);
  }

  sentenceSegments(usage: UsageModel): HighlightSegmentModel[] {
    return this.highlightSegmenter.splitBySpan(
      usage.sentence,
      usage.sentenceStart,
      usage.sentenceEnd
    );
  }

  translationSegments(usage: UsageModel): HighlightSegmentModel[] {
    return this.highlightSegmenter.splitBySpan(
      usage.translation,
      usage.translationStart,
      usage.translationEnd
    );
  }

  translationValue(language: string): string {
    return this.translationDrafts()[language] ?? '';
  }
}
