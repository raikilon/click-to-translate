export interface LanguageDto {
  id: string;
  code: string;
  name: string;
}

const BCP47_CODE_PATTERN = /^[A-Za-z]{2,3}(?:-[A-Za-z0-9]{2,8})*$/;

export function isLanguageDto(value: unknown): value is LanguageDto {
  if (typeof value !== "object" || value === null) {
    return false;
  }

  const candidate = value as Partial<LanguageDto>;

  return (
    typeof candidate.id === "string" &&
    candidate.id.length > 0 &&
    typeof candidate.code === "string" &&
    BCP47_CODE_PATTERN.test(candidate.code) &&
    typeof candidate.name === "string" &&
    candidate.name.length > 0
  );
}
