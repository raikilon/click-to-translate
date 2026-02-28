export interface ContextReducer {
  reduce(input: { selectedWord: string; textAround: string }): string;
}
