interface SubtitleEntry {
  text: string;
  capturedAtMs: number;
}

export class SubtitleBuffer {
  private entries: SubtitleEntry[] = [];

  constructor(
    private readonly maxEntries = 8,
    private readonly maxAgeMs = 20_000,
  ) {}

  append(text: string, nowMs = Date.now()): void {
    const normalized = this.normalizeWhitespace(text);
    if (!normalized) {
      return;
    }

    this.prune(nowMs);

    const latest = this.entries[this.entries.length - 1];
    if (latest?.text === normalized) {
      latest.capturedAtMs = nowMs;
      return;
    }

    this.entries.push({ text: normalized, capturedAtMs: nowMs });
    if (this.entries.length > this.maxEntries) {
      this.entries = this.entries.slice(this.entries.length - this.maxEntries);
    }
  }

  getJoinedText(nowMs = Date.now()): string {
    this.prune(nowMs);
    return this.entries.map((entry) => entry.text).join(" ").trim();
  }

  clear(): void {
    this.entries = [];
  }

  private normalizeWhitespace(value: string): string {
    return value.replace(/\s+/g, " ").trim();
  }

  private prune(nowMs: number): void {
    const minTimestamp = nowMs - this.maxAgeMs;
    this.entries = this.entries.filter((entry) => entry.capturedAtMs >= minTimestamp);
  }
}
