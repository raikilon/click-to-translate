import { CommonModule } from '@angular/common';
import { httpResource } from '@angular/common/http';
import { Component, computed, effect, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HighlightStrategyFactory } from '../../application/highlight/highlight-strategy.factory';
import { SpanHighlighterService } from '../../application/highlight/span-highlighter.service';
import { VocabularyFacade } from '../../application/vocabulary.facade';
import { HighlightSegmentModel } from '../../domain/highlight-segment.model';
import { emptyPageModel } from '../../domain/page.model';
import { UsageModel } from '../../domain/usage.model';
import { VocabularyEntryModel } from '../../domain/vocabulary-entry.model';
import { PaginationComponent } from '../shared/pagination.component';

@Component({
  selector: 'app-entry-details-page',
  standalone: true,
  imports: [CommonModule, PaginationComponent],
  templateUrl: './entry-details-page.component.html'
})
export class EntryDetailsPageComponent {
  protected readonly entryId = signal(this.readEntryId());
  protected readonly usagesPageIndex = signal(0);
  protected readonly usagesPageSize = 10;

  protected readonly termCustomizationDraft = signal('');
  protected readonly translationDrafts = signal<Record<string, string>>({});
  protected readonly selectedLanguage = signal('');
  protected readonly selectedLanguageText = signal('');

  protected readonly entryResource = httpResource(() => {
    const entryId = this.entryId();
    if (entryId <= 0) {
      return undefined;
    }

    return this.vocabularyFacade.buildEntryRequest(entryId);
  }, {
    parse: (payload: unknown) => this.vocabularyFacade.parseEntry(payload)
  });

  protected readonly usagesResource = httpResource(
    () => {
      if (this.entryId() <= 0) {
        return undefined;
      }

      return this.vocabularyFacade.buildUsagesRequest(
        this.entryId(),
        this.usagesPageIndex(),
        this.usagesPageSize
      );
    },
    {
      defaultValue: emptyPageModel<UsageModel>(),
      parse: (payload: unknown) => this.vocabularyFacade.parseUsagesPage(payload)
    }
  );

  protected readonly languageResource = httpResource(
    () => this.vocabularyFacade.buildLanguagesRequest(),
    {
      defaultValue: [] as string[],
      parse: (payload: unknown) => this.vocabularyFacade.parseLanguages(payload)
    }
  );

  protected readonly highlightClassName = computed(() =>
    this.highlightStrategyFactory.currentClassName()
  );

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly vocabularyFacade: VocabularyFacade,
    private readonly spanHighlighterService: SpanHighlighterService,
    private readonly highlightStrategyFactory: HighlightStrategyFactory
  ) {
    effect(() => {
      const entry = this.entryResource.value();
      if (!entry) {
        return;
      }

      this.termCustomizationDraft.set(entry.termCustomization ?? entry.term);

      const drafts: Record<string, string> = {};
      for (const translation of entry.translations) {
        drafts[translation.language] = translation.term;
      }

      this.translationDrafts.set(drafts);
    });
  }

  backToList(): void {
    void this.router.navigate(['/']);
  }

  goToSettings(): void {
    void this.router.navigate(['/settings']);
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

    const current = { ...this.translationDrafts() };
    current[language] = target.value;
    this.translationDrafts.set(current);
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
    const value = this.termCustomizationDraft().trim();
    if (!value) {
      return;
    }

    await this.vocabularyFacade.updateTerm(this.entryId(), value);
    this.entryResource.reload();
  }

  async saveTranslation(language: string): Promise<void> {
    const translation = this.translationDrafts()[language]?.trim();
    if (!translation) {
      return;
    }

    await this.vocabularyFacade.updateTranslation(this.entryId(), language, translation);
    this.entryResource.reload();
  }

  async saveSelectedTranslation(): Promise<void> {
    const language = this.selectedLanguage();
    const translation = this.selectedLanguageText().trim();
    if (!language || !translation) {
      return;
    }

    await this.vocabularyFacade.updateTranslation(this.entryId(), language, translation);
    this.selectedLanguageText.set('');
    this.entryResource.reload();
  }

  async deleteEntry(): Promise<void> {
    const confirmed = window.confirm('Delete this entry?');
    if (!confirmed) {
      return;
    }

    await this.vocabularyFacade.deleteEntry(this.entryId());
    await this.router.navigate(['/']);
  }

  async deleteUsage(usageId: number): Promise<void> {
    const confirmed = window.confirm('Delete this usage?');
    if (!confirmed) {
      return;
    }

    await this.vocabularyFacade.deleteUsage(this.entryId(), usageId);
    this.usagesResource.reload();
  }

  async starUsage(usageId: number): Promise<void> {
    await this.vocabularyFacade.starUsage(this.entryId(), usageId);
    this.usagesResource.reload();
  }

  changeUsagesPage(page: number): void {
    this.usagesPageIndex.set(page);
  }

  sentenceSegments(usage: UsageModel): HighlightSegmentModel[] {
    return this.spanHighlighterService.buildBySpan(
      usage.sentence,
      usage.sentenceStart,
      usage.sentenceEnd
    );
  }

  translationSegments(usage: UsageModel): HighlightSegmentModel[] {
    return this.spanHighlighterService.buildBySpan(
      usage.translation,
      usage.translationStart,
      usage.translationEnd
    );
  }

  translationValue(language: string): string {
    return this.translationDrafts()[language] ?? '';
  }

  private readEntryId(): number {
    const value = Number(this.route.snapshot.paramMap.get('entryId'));
    if (Number.isFinite(value) && value > 0) {
      return value;
    }

    return 0;
  }
}
