export interface UsageDto {
  usageId: number;
  entryId: number;
  sentence: string;
  sentenceStart: number;
  sentenceEnd: number;
  translation: string;
  translationStart?: number;
  translationEnd?: number;
  language: string;
  starred: boolean;
  lastEdit: string;
  createdAt: string;
}
