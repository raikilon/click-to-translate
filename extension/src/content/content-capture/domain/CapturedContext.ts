export class CapturedContext {
  constructor(
    readonly word: string,
    readonly context: string,
    readonly anchorX: number,
    readonly anchorY: number,
  ) {}
}
