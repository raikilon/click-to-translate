export interface PageModel<T> {
  items: T[];
  page: number;
  size: number;
  totalItems: number;
  totalPages: number;
  hasNext: boolean;
}

export function emptyPageModel<T>(): PageModel<T> {
  return {
    items: [],
    page: 0,
    size: 0,
    totalItems: 0,
    totalPages: 0,
    hasNext: false
  };
}
