import { Injectable } from '@angular/core';
import { PageModel } from '../../domain/page.model';
import { TermTranslationModel } from '../../domain/term-translation.model';
import { UsageModel } from '../../domain/usage.model';
import { VocabularyEntryModel } from '../../domain/vocabulary-entry.model';
import { EntryDto } from '../dto/entry.dto';
import { PageEnvelopeDto } from '../dto/page-envelope.dto';
import { TermDto } from '../dto/term.dto';
import { UsageDto } from '../dto/usage.dto';

@Injectable({ providedIn: 'root' })
export class VocabularyMapper {
  toEntryPageModel(dto: PageEnvelopeDto<EntryDto>): PageModel<VocabularyEntryModel> {
    return {
      items: dto.items.map((item) => this.toEntryModel(item)),
      page: dto.page,
      size: dto.size,
      totalItems: dto.totalItems,
      totalPages: dto.totalPages,
      hasNext: dto.hasNext
    };
  }

  toEntryModel(dto: EntryDto): VocabularyEntryModel {
    return {
      entryId: dto.entryId,
      language: dto.language.toUpperCase(),
      term: dto.term,
      termCustomization: dto.termCustomization,
      translations: dto.translations.map((translation) =>
        this.toTranslationModel(translation)
      ),
      usages: dto.usages.map((usage) => this.toUsageModel(usage)),
      lastEdit: dto.lastEdit,
      createdAt: dto.createdAt
    };
  }

  toUsageModel(dto: UsageDto): UsageModel {
    return {
      usageId: dto.usageId,
      entryId: dto.entryId,
      sentence: dto.sentence,
      sentenceStart: dto.sentenceStart,
      sentenceEnd: dto.sentenceEnd,
      translation: dto.translation,
      translationStart: dto.translationStart,
      translationEnd: dto.translationEnd,
      language: dto.language.toUpperCase(),
      starred: dto.starred,
      lastEdit: dto.lastEdit,
      createdAt: dto.createdAt
    };
  }

  private toTranslationModel(dto: TermDto): TermTranslationModel {
    return {
      language: dto.language.toUpperCase(),
      term: dto.term
    };
  }
}
