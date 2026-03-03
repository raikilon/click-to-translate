import { HighlightMode } from '../../../authentication/domain/highlight-mode';

export interface HighlightStrategy {
  readonly mode: HighlightMode;
  className(): string;
}
