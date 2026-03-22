import { Injectable } from '@angular/core';
import { ParsedSearchQuery } from './search-query.model';

@Injectable({ providedIn: 'root' })
export class SearchQueryParser {
  private static readonly MIN_QUERY_LENGTH = 3;

  parse(query: string): ParsedSearchQuery {
    const trimmed = query.trim();
    if (!trimmed) {
      return { mode: 'all', query: '' };
    }

    const tokens = trimmed.split(/\s+/);
    const nonLanguageTokens: string[] = [];
    let language: string | undefined;

    for (const token of tokens) {
      const tokenValue = token.trim();
      if (!tokenValue) {
        continue;
      }

      if (!language && tokenValue.startsWith('@')) {
        const candidate = tokenValue.slice(1).toUpperCase();
        if (/^[A-Z]{2,3}$/.test(candidate)) {
          language = candidate;
          continue;
        }
      }

      nonLanguageTokens.push(tokenValue);
    }

    const normalizedQuery = nonLanguageTokens.join(' ').trim();

    if (language) {
      return {
        mode: 'language',
        query: normalizedQuery,
        language,
      };
    }

    if (normalizedQuery.length >= SearchQueryParser.MIN_QUERY_LENGTH) {
      return { mode: 'query', query: normalizedQuery };
    }

    return { mode: 'all', query: '' };
  }

  extractLanguageToken(query: string): string | null {
    const words = query.trim().split(/\s+/);
    const lastWord = words.at(-1);
    if (!lastWord || !lastWord.startsWith('@')) {
      return null;
    }

    return lastWord.slice(1).toUpperCase();
  }

  applyLanguageToken(query: string, language: string): string {
    const trimmed = query.trim();
    const words = trimmed.split(/\s+/).filter((word) => word.length > 0);
    const normalizedLanguage = `@${language.toUpperCase()}`;

    if (words.length === 0) {
      return normalizedLanguage;
    }

    const lastWord = words.at(-1);
    if (lastWord && lastWord.startsWith('@')) {
      words[words.length - 1] = normalizedLanguage;
      return words.join(' ');
    }

    return `${trimmed} ${normalizedLanguage}`.trim();
  }
}
