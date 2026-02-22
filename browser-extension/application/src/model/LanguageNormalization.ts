import type { LanguageDto } from "@domain";
import { isLanguageDto } from "@domain";

const LANGUAGE_NAME_BY_CODE: Record<string, string> = {
  da: "Danish",
  de: "German",
  en: "English",
  es: "Spanish",
  et: "Estonian",
  fi: "Finnish",
  fr: "French",
  it: "Italian",
  nb: "Norwegian Bokmal",
  pt: "Portuguese",
  sl: "Slovenian",
  sv: "Swedish",
};

function hasText(value: string): boolean {
  return value.trim().length > 0;
}

function normalizeLanguageCode(value: string): string {
  return value.trim().toLowerCase();
}

export function languageFromCode(code: string): LanguageDto | null {
  const normalizedCode = normalizeLanguageCode(code);
  if (!normalizedCode) {
    return null;
  }

  return {
    id: normalizedCode,
    code: normalizedCode,
    name: LANGUAGE_NAME_BY_CODE[normalizedCode] ?? normalizedCode.toUpperCase(),
  };
}

function normalizeLanguageCandidate(candidate: unknown): LanguageDto | null {
  if (typeof candidate === "string") {
    return languageFromCode(candidate);
  }

  if (!candidate || typeof candidate !== "object") {
    return null;
  }

  if (isLanguageDto(candidate)) {
    const fromCode = languageFromCode(candidate.code);
    if (!fromCode) {
      return null;
    }

    return {
      id: hasText(candidate.id)
        ? normalizeLanguageCode(candidate.id)
        : fromCode.id,
      code: fromCode.code,
      name: hasText(candidate.name) ? candidate.name.trim() : fromCode.name,
    };
  }

  const shape = candidate as {
    id?: unknown;
    code?: unknown;
    name?: unknown;
    value?: unknown;
  };

  const rawCode =
    typeof shape.code === "string"
      ? shape.code
      : typeof shape.id === "string"
        ? shape.id
        : typeof shape.value === "string"
          ? shape.value
          : "";

  const fromCode = languageFromCode(rawCode);
  if (!fromCode) {
    return null;
  }

  const name =
    typeof shape.name === "string" && hasText(shape.name)
      ? shape.name.trim()
      : fromCode.name;
  const id =
    typeof shape.id === "string" && hasText(shape.id)
      ? normalizeLanguageCode(shape.id)
      : fromCode.id;

  return {
    id,
    code: fromCode.code,
    name,
  };
}

export function normalizeLanguageList(value: unknown): LanguageDto[] {
  if (!Array.isArray(value)) {
    return [];
  }

  const byCode = new Map<string, LanguageDto>();
  for (const candidate of value) {
    const normalized = normalizeLanguageCandidate(candidate);
    if (normalized) {
      byCode.set(normalized.code, normalized);
    }
  }

  return Array.from(byCode.values()).sort((left, right) =>
    left.name.localeCompare(right.name),
  );
}

export function findLanguageById(
  languages: LanguageDto[],
  selectedId: string | null,
): LanguageDto | null {
  if (!selectedId) {
    return null;
  }

  const normalizedSelectedId = normalizeLanguageCode(selectedId);
  if (!normalizedSelectedId) {
    return null;
  }

  return (
    languages.find(
      (language) =>
        normalizeLanguageCode(language.id) === normalizedSelectedId ||
        normalizeLanguageCode(language.code) === normalizedSelectedId,
    ) ?? null
  );
}
