import { CommonModule } from '@angular/common';
import { httpResource } from '@angular/common/http';
import { Component, computed, input, output, signal } from '@angular/core';
import { Router } from '@angular/router';
import { HighlightSegmentModel } from '../../domain/highlight-segment.model';
import { emptyPageModel } from '../../domain/page.model';
import { UsageModel } from '../../domain/usage.model';
import { VocabularyEntryModel } from '../../domain/vocabulary-entry.model';
import { SpanHighlighterService } from '../../application/highlight/span-highlighter.service';
import { VocabularyFacade } from '../../application/vocabulary.facade';

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

  protected readonly previewUsagesResource = httpResource(() => {
    if (!this.expanded()) {
      return undefined;
    }

    return this.vocabularyFacade.buildUsagePreviewRequest(this.entry().entryId);
  }, {
    defaultValue: emptyPageModel<UsageModel>(),
    parse: (payload: unknown) => this.vocabularyFacade.parseUsagesPage(payload)
  });

  constructor(
    private readonly router: Router,
    private readonly vocabularyFacade: VocabularyFacade,
    private readonly spanHighlighterService: SpanHighlighterService
  ) {}

  toggleExpanded(): void {
    this.expanded.update((value) => !value);
    if (this.expanded()) {
      this.previewUsagesResource.reload();
    }
  }

  openDetails(event: Event): void {
    event.stopPropagation();
    void this.router.navigate(['/entries', this.entry().entryId]);
  }

  requestDelete(event: Event): void {
    event.stopPropagation();
    this.deleteRequested.emit(this.entry().entryId);
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
}
