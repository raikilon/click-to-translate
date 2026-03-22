import { Injectable } from '@angular/core';
import { UsageModel } from './usage.model';

@Injectable({ providedIn: 'root' })
export class UsageOrdering {
  sortByPriority(usages: UsageModel[]): UsageModel[] {
    return usages.slice().sort((left, right) => {
      if (left.starred !== right.starred) {
        return left.starred ? -1 : 1;
      }

      const leftDate = Date.parse(left.createdAt);
      const rightDate = Date.parse(right.createdAt);
      const leftTimestamp = Number.isFinite(leftDate) ? leftDate : 0;
      const rightTimestamp = Number.isFinite(rightDate) ? rightDate : 0;

      return rightTimestamp - leftTimestamp;
    });
  }
}
