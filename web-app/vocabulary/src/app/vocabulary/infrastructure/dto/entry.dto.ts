import { TermDto } from './term.dto';
import { UsageDto } from './usage.dto';

export interface EntryDto {
  entryId: number;
  language: string;
  term: string;
  termCustomization?: string;
  translations: TermDto[];
  usages: UsageDto[];
  lastEdit: string;
  createdAt: string;
}
