import { Injectable, signal } from '@angular/core';
import { HighlightMode } from '../domain/highlight-mode';

@Injectable({ providedIn: 'root' })
export class HighlightPreferenceService {
  private readonly storageKey = 'vocabulary.preferences.highlight-mode';
  readonly mode = signal<HighlightMode>(this.readMode());

  setMode(mode: HighlightMode): void {
    this.mode.set(mode);
    localStorage.setItem(this.storageKey, mode);
  }

  private readMode(): HighlightMode {
    const stored = localStorage.getItem(this.storageKey);
    return stored === 'box' ? 'box' : 'color';
  }
}
