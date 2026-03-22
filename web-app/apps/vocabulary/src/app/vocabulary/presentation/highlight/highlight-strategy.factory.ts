import { Injectable } from '@angular/core';
import { HighlightPreferenceService } from '../../../settings/preferences/highlight-preference.service';
import { BoxHighlightStrategy } from './box-highlight.strategy';
import { ColorHighlightStrategy } from './color-highlight.strategy';
import { HighlightStrategy } from './highlight-strategy';

@Injectable({ providedIn: 'root' })
export class HighlightStrategyFactory {
  constructor(
    private readonly preferences: HighlightPreferenceService,
    private readonly colorStrategy: ColorHighlightStrategy,
    private readonly boxStrategy: BoxHighlightStrategy,
  ) {}

  current(): HighlightStrategy {
    return this.preferences.mode() === 'box' ? this.boxStrategy : this.colorStrategy;
  }

  currentClassName(): string {
    return this.current().className();
  }
}
