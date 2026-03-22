export class CapturedWord {
  constructor(
    readonly word: string,
    readonly textNode: Text,
    readonly start: number,
    readonly end: number,
    readonly anchorX: number,
    readonly anchorY: number,
  ) {}
}
