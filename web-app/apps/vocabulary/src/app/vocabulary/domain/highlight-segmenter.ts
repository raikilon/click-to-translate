import { Injectable } from '@angular/core';
import { HighlightSegmentModel } from './highlight-segment.model';

@Injectable({ providedIn: 'root' })
export class HighlightSegmenter {
  splitBySpan(text: string, start?: number, end?: number): HighlightSegmentModel[] {
    if (start === undefined || end === undefined || start === end) {
      return [{ value: text, highlighted: false }];
    }

    const segments: HighlightSegmentModel[] = [];

    if (start > 0) {
      segments.push({ value: text.slice(0, start), highlighted: false });
    }

    segments.push({
      value: text.slice(start, end),
      highlighted: true,
    });

    if (end < text.length) {
      segments.push({ value: text.slice(end), highlighted: false });
    }

    return segments;
  }
}
