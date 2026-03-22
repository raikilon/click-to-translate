import { CommonModule } from '@angular/common';
import { Component, input, output, signal } from '@angular/core';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './search-bar.component.html',
})
export class SearchBarComponent {
  readonly query = input.required<string>();
  readonly languageSuggestions = input.required<string[]>();
  readonly suggestionsLoading = input.required<boolean>();

  readonly queryChange = output<string>();
  readonly languageSuggestionSelected = output<string>();

  protected readonly focused = signal(false);

  onInput(event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    this.queryChange.emit(target.value);
  }

  onFocus(): void {
    this.focused.set(true);
  }

  onBlur(): void {
    setTimeout(() => {
      this.focused.set(false);
    }, 120);
  }

  selectLanguage(language: string): void {
    this.languageSuggestionSelected.emit(language);
    this.focused.set(false);
  }
}
