import { TermTranslationModel } from './term-translation.model';
import { UsageModel } from './usage.model';

export interface VocabularyEntryModel {
  entryId: number;
  language: string;
  term: string;
  termCustomization?: string;
  translations: TermTranslationModel[];
  usages: UsageModel[];
  lastEdit: string;
  createdAt: string;
}
