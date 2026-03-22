import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';
import { VocabularyEntryModel } from '../../../domain/vocabulary-entry.model';
import { EntryRowComponent } from '../entry-row/entry-row.component';

@Component({
  selector: 'app-entry-list',
  standalone: true,
  imports: [CommonModule, EntryRowComponent],
  templateUrl: './entry-list.component.html',
})
export class EntryListComponent {
  readonly entries = input.required<VocabularyEntryModel[]>();
  readonly highlightClassName = input.required<string>();

  readonly deleteRequested = output<number>();

  onDelete(entryId: number): void {
    this.deleteRequested.emit(entryId);
  }
}
