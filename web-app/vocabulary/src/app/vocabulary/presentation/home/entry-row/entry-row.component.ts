import { CommonModule } from '@angular/common';
import { Component, computed, input, output, signal } from '@angular/core';
import { Router } from '@angular/router';
import { appRouteCommands } from '../../../../routing/route.constants';
import { HighlightSegmentModel } from '../../../domain/highlight-segment.model';
import { HighlightSegmenter } from '../../../domain/highlight-segmenter';
import { UsageModel } from '../../../domain/usage.model';
import { UsageOrdering } from '../../../domain/usage-ordering';
import { VocabularyEntryModel } from '../../../domain/vocabulary-entry.model';

@Component({
  selector: 'app-entry-row',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './entry-row.component.html'
})
export class EntryRowComponent {
  readonly entry = input.required<VocabularyEntryModel>();
  readonly highlightClassName = input.required<string>();
  readonly deleteRequested = output<number>();

  protected readonly expanded = signal(false);
  protected readonly hasOneTranslation = computed(
    () => this.entry().translations.length === 1
  );
  protected readonly translationLanguages = computed(() =>
    this.entry().translations.map((translation) => translation.language.toUpperCase())
  );
  protected readonly previewUsages = computed(() =>
    this.usageOrdering.sortByPriority(this.entry().usages).slice(0, 3)
  );

  constructor(
    private readonly router: Router,
    private readonly highlightSegmenter: HighlightSegmenter,
    private readonly usageOrdering: UsageOrdering
  ) {}

  toggleExpanded(): void {
    this.expanded.update((value) => !value);
  }

  openDetails(event: Event): void {
    event.stopPropagation();
    void this.router.navigate(appRouteCommands.entryDetails(this.entry().entryId));
  }

  requestDelete(event: Event): void {
    event.stopPropagation();
    this.deleteRequested.emit(this.entry().entryId);
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
