import type { Anchor } from "../input/Anchor";
import type { SourceDto } from "./Source";
import type { SourceMetadataDto } from "./SourceMetadata";

export interface CaptureResult {
  word: string;
  sentence: string;
  source: SourceDto;
  sourceMetadata?: SourceMetadataDto;
  anchor: Anchor;
}
