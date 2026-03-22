import type { LanguageId } from './language-id';

export function normalizeLanguageIds(values: readonly string[]): LanguageId[] {
  const normalized = values
    .map((value) => value.trim())
    .filter((value) => value.length > 0)
    .map((value) => value.toUpperCase());

  return Array.from(new Set(normalized)).sort((left, right) => left.localeCompare(right));
}
