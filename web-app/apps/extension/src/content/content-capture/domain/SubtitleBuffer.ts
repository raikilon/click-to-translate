import { SubtitleEvent } from "./SubtitleEvent";

export class SubtitleBuffer {
  private entries: SubtitleEvent[] = [];

  constructor(
    private readonly maxEntries: number = 8,
    private readonly maxAgeMs: number = 20000,
  ) {}

  append(event: SubtitleEvent): void {
    const text = this.normalize(event.text);
    if (!text) {
      return;
    }

    this.prune(event.capturedAtMs);

    const latest = this.entries[this.entries.length - 1];
    if (latest && latest.text === text) {
      this.entries[this.entries.length - 1] = new SubtitleEvent(text, event.capturedAtMs);
      return;
    }

    this.entries.push(new SubtitleEvent(text, event.capturedAtMs));
    if (this.entries.length > this.maxEntries) {
      this.entries = this.entries.slice(this.entries.length - this.maxEntries);
    }
  }

  getJoinedText(nowMs: number = Date.now()): string {
    this.prune(nowMs);
    return this.entries.map((entry) => entry.text).join(" ").trim();
  }

  hasRecentEntries(nowMs: number = Date.now()): boolean {
    this.prune(nowMs);
    return this.entries.length > 0;
  }

  clear(): void {
    this.entries = [];
  }

  private prune(nowMs: number): void {
    const minTimestamp = nowMs - this.maxAgeMs;
    this.entries = this.entries.filter((entry) => entry.capturedAtMs >= minTimestamp);
  }

  private normalize(value: string): string {
    return value.replace(/\s+/g, " ").trim();
  }
}





