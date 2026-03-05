import { HighlightMode } from '../../../settings/preferences/highlight-mode';

export interface HighlightStrategy {
  readonly mode: HighlightMode;
  className(): string;
}
