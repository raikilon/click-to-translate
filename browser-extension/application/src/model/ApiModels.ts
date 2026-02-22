export interface PostSegmentResponse {
  translationText?: string;
  segmentId?: string;
  word?: string;
  sentence?: string;
  translatedWord?: string;
  translatedSentence?: string;
}

export interface ApiError {
  status: number;
  message: string;
  code?: string;
}
