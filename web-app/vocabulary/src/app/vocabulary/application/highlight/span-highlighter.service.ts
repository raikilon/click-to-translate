import { Injectable } from '@angular/core';
import { HighlightSegmentModel } from '../../domain/highlight-segment.model';

@Injectable({ providedIn: 'root' })
export class SpanHighlighterService {
  buildBySpan(
    text: string,
    start?: number,
    end?: number
  ): HighlightSegmentModel[] {
    if (
      start === undefined ||
      end === undefined ||
      !Number.isInteger(start) ||
      !Number.isInteger(end)
    ) {
      return [{ value: text, highlighted: false }];
    }

    const normalizedStart = Math.max(0, Math.min(start, text.length));
    const normalizedEnd = Math.max(normalizedStart, Math.min(end, text.length));

    if (normalizedStart === normalizedEnd) {
      return [{ value: text, highlighted: false }];
    }

    const segments: HighlightSegmentModel[] = [];

    if (normalizedStart > 0) {
      segments.push({
        value: text.slice(0, normalizedStart),
        highlighted: false
      });
    }

    segments.push({
      value: text.slice(normalizedStart, normalizedEnd),
      highlighted: true
    });

    if (normalizedEnd < text.length) {
      segments.push({
        value: text.slice(normalizedEnd),
        highlighted: false
      });
    }

    return segments;
  }
}
