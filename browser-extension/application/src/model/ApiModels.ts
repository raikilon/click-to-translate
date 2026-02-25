export interface PostSegmentResponse {
  translatedWord?: string;
}

export interface ApiError {
  status: number;
  message: string;
  code?: string;
}
