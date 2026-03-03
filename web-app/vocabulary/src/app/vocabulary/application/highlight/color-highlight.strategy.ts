import { Injectable } from '@angular/core';
import { HighlightMode } from '../../../authentication/domain/highlight-mode';
import { HighlightStrategy } from './highlight-strategy';

@Injectable({ providedIn: 'root' })
export class ColorHighlightStrategy implements HighlightStrategy {
  readonly mode: HighlightMode = 'color';

  className(): string {
    return 'rounded-sm bg-amber-200 px-1 text-slate-900';
  }
}
