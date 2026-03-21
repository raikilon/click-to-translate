import { CommonModule } from '@angular/common';
import { Component, computed, inject } from '@angular/core';
import { Router } from '@angular/router';
import { appRouteCommands } from '../../../routing/route.constants';
import { HighlightSegmentModel } from '../../domain/highlight-segment.model';
import { HighlightSegmenter } from '../../domain/highlight-segmenter';
import { UsageModel } from '../../domain/usage.model';
import { EntryDetailsPageStore } from '../../infrastructure/state/entry-details-page.store';
import { HighlightStrategyFactory } from '../highlight/highlight-strategy.factory';
import {
  EditableFieldEditorContext,
  EditableFieldEditorDirective,
  EditableFieldComponent
} from '../shared/editable-field/editable-field.component';
import { PaginationComponent } from '../shared/pagination/pagination.component';

@Component({
  selector: 'app-entry-details-page',
  standalone: true,
  imports: [
    CommonModule,
    PaginationComponent,
    EditableFieldComponent,
    EditableFieldEditorDirective
  ],
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

    if (error.message.trim()) {
      return error.message;
    }

    return 'Failed to load entry.';
  });
  protected readonly pagedUsages = this.store.pagedUsages;
  protected readonly usagesPageIndex = this.store.usagesPageIndex;
  protected readonly usagesTotalPages = this.store.usagesTotalPages;
  protected readonly usagesHasNext = this.store.usagesHasNext;
  protected readonly highlightClassName = computed(() =>
    this.highlightStrategyFactory.currentClassName()
  );
  protected readonly saveTermCustomization = async (term: string): Promise<void> =>
    this.store.saveTerm(term);

  private readonly translationSaveActions = new Map<
    string,
    (value: string) => Promise<void>
  >();

  constructor(
    private readonly router: Router,
    private readonly highlightStrategyFactory: HighlightStrategyFactory,
    private readonly highlightSegmenter: HighlightSegmenter
  ) {}

  backToList(): void {
    void this.router.navigate(appRouteCommands.home());
  }

  goToSettings(): void {
    void this.router.navigate(appRouteCommands.settings());
  }

  applyEditorValue(
    event: Event,
    setValue: EditableFieldEditorContext['setValue']
  ): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    setValue(target.value);
  }

  saveTranslationAction(language: string): (value: string) => Promise<void> {
    const key = language.toUpperCase();
    const existingAction = this.translationSaveActions.get(key);
    if (existingAction) {
      return existingAction;
    }

    const action = async (translation: string): Promise<void> =>
      this.store.saveTranslation(key, translation);
    this.translationSaveActions.set(key, action);
    return action;
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
}
