export class FixedWindowContextExtractor {
  constructor(private readonly maxChars: number = 160) {}

  extract(text: string, word: string): string {
    const cleanText = this.normalize(text);
    const cleanWord = this.normalize(word);
    if (!cleanText) {
      return cleanWord;
    }

    if (cleanText.length <= this.maxChars) {
      return cleanText;
    }

    const lowerText = cleanText.toLowerCase();
    const lowerWord = cleanWord.toLowerCase();
    const index = lowerText.indexOf(lowerWord);
    if (index < 0) {
      return cleanText.slice(0, this.maxChars).trim();
    }

    const slack = Math.max(0, this.maxChars - cleanWord.length);
    let start = Math.max(0, index - Math.floor(slack / 2));
    let end = start + this.maxChars;

    if (end > cleanText.length) {
      end = cleanText.length;
      start = Math.max(0, end - this.maxChars);
    }

    return cleanText.slice(start, end).trim();
  }

  private normalize(value: string): string {
    return value.replace(/\s+/g, " ").trim();
  }
}





