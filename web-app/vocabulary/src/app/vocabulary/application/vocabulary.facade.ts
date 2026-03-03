import { HttpResourceRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageModel } from '../domain/page.model';
import { UsageModel } from '../domain/usage.model';
import { VocabularyEntryModel } from '../domain/vocabulary-entry.model';
import { EntryDto } from '../infrastructure/dto/entry.dto';
import { PageEnvelopeDto } from '../infrastructure/dto/page-envelope.dto';
import { UsageDto } from '../infrastructure/dto/usage.dto';
import { VocabularyMapper } from '../infrastructure/mappers/vocabulary.mapper';
import { VocabularyHttpGateway } from '../infrastructure/vocabulary-http.gateway';
import { SearchQueryParserService } from './search-query-parser.service';
import { TranslationLanguagesHttpGateway } from '../infrastructure/translation-languages-http.gateway';

@Injectable({ providedIn: 'root' })
export class VocabularyFacade {
  constructor(
    private readonly parser: SearchQueryParserService,
    private readonly mapper: VocabularyMapper,
    private readonly vocabularyGateway: VocabularyHttpGateway,
    private readonly translationLanguagesGateway: TranslationLanguagesHttpGateway
  ) {}

  buildEntriesRequest(rawQuery: string, page: number, size: number): HttpResourceRequest {
    const parsed = this.parser.parse(rawQuery);

    if (parsed.mode === 'language' && parsed.language) {
      return this.vocabularyGateway.listByLanguageRequest(parsed.language, page, size);
    }

    if (parsed.mode === 'query') {
      return this.vocabularyGateway.searchRequest(parsed.query, page, size);
    }

    return this.vocabularyGateway.listAllRequest(page, size);
  }

  buildEntryRequest(entryId: number): HttpResourceRequest {
    return this.vocabularyGateway.getEntryRequest(entryId);
  }

  buildUsagesRequest(
    entryId: number,
    page: number,
    size: number
  ): HttpResourceRequest {
    return this.vocabularyGateway.listUsagesRequest(entryId, page, size);
  }

  buildUsagePreviewRequest(entryId: number): HttpResourceRequest {
    return this.vocabularyGateway.listUsagePreviewRequest(entryId);
  }

  buildLanguagesRequest(): HttpResourceRequest {
    return this.translationLanguagesGateway.listLanguagesRequest();
  }

  parseEntriesPage(payload: unknown): PageModel<VocabularyEntryModel> {
    return this.mapper.toEntryPageModel(payload as PageEnvelopeDto<EntryDto>);
  }

  parseUsagesPage(payload: unknown): PageModel<UsageModel> {
    return this.mapper.toUsagePageModel(payload as PageEnvelopeDto<UsageDto>);
  }

  parseEntry(payload: unknown): VocabularyEntryModel {
    return this.mapper.toEntryModel(payload as EntryDto);
  }

  parseLanguages(payload: unknown): string[] {
    if (!Array.isArray(payload)) {
      return [];
    }

    return payload
      .filter((value): value is string => typeof value === 'string')
      .map((value) => value.toUpperCase())
      .filter((value, index, array) => array.indexOf(value) === index)
      .sort((a, b) => a.localeCompare(b));
  }

  languageSuggestions(languages: string[], rawQuery: string): string[] {
    const token = this.parser.extractLanguageToken(rawQuery);
    if (token === null) {
      return [];
    }

    if (!token) {
      return languages;
    }

    return languages.filter((language) => language.startsWith(token));
  }

  applyLanguageSuggestion(rawQuery: string, language: string): string {
    return this.parser.applyLanguage(rawQuery, language);
  }

  async updateTerm(entryId: number, term: string): Promise<void> {
    await this.vocabularyGateway.updateTerm(entryId, term);
  }

  async updateTranslation(
    entryId: number,
    language: string,
    translation: string
  ): Promise<void> {
    await this.vocabularyGateway.updateTranslation(entryId, language, translation);
  }

  async deleteEntry(entryId: number): Promise<void> {
    await this.vocabularyGateway.deleteEntry(entryId);
  }

  async deleteUsage(entryId: number, usageId: number): Promise<void> {
    await this.vocabularyGateway.deleteUsage(entryId, usageId);
  }

  async starUsage(entryId: number, usageId: number): Promise<void> {
    await this.vocabularyGateway.starUsage(entryId, usageId);
  }
}
