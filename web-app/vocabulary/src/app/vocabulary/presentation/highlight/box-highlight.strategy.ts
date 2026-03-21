import { Injectable } from '@angular/core';
import { HighlightMode } from '../../../settings/preferences/highlight-mode';
import { HighlightStrategy } from './highlight-strategy';

@Injectable({ providedIn: 'root' })
export class BoxHighlightStrategy implements HighlightStrategy {
  readonly mode: HighlightMode = 'box';

  className(): string {
    return 'rounded-sm px-1 outline outline-2 outline-cyan-500';
  }
}
