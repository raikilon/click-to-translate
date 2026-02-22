import type { LanguageDto } from "./Language";
import type { SourceDto } from "./Source";
import type { SourceMetadataDto } from "./SourceMetadata";

// ISO-8601 UTC timestamp string (for example: 2026-02-22T12:00:00Z)
export type InstantString = string;

export interface SegmentBundleDto {
  word: string;
  sentence: string;
  sourceLanguage: LanguageDto;
  targetLanguage: LanguageDto;
  source: SourceDto;
  sourceMetadata?: SourceMetadataDto;
  occurredAt: InstantString;
}
