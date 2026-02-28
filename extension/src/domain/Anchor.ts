import { DomainValidationError } from "./DomainValidationError";

export class Anchor {
  readonly x: number;
  readonly y: number;

  constructor(x: number, y: number) {
    if (!Number.isFinite(x) || !Number.isFinite(y)) {
      throw new DomainValidationError("anchor coordinates must be finite numbers.");
    }

    this.x = x;
    this.y = y;
  }
}
