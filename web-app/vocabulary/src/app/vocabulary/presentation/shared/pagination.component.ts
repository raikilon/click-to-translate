import { CommonModule } from '@angular/common';
import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagination.component.html'
})
export class PaginationComponent {
  readonly page = input.required<number>();
  readonly totalPages = input.required<number>();
  readonly hasNext = input.required<boolean>();

  readonly pageChange = output<number>();

  previousPage(): void {
    const current = this.page();
    if (current <= 0) {
      return;
    }

    this.pageChange.emit(current - 1);
  }

  nextPage(): void {
    const current = this.page();
    if (!this.hasNext()) {
      return;
    }

    this.pageChange.emit(current + 1);
  }
}
