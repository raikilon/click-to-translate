import type { LanguageDto, SegmentBundleDto } from "@domain";
import type { AuthSession } from "../model/AuthSession";
import type { PostSegmentResponse } from "../model/ApiModels";

export interface ApiClient {
  getLanguages(accessToken: string | null): Promise<LanguageDto[]>;
  postSegment(
    accessToken: string,
    bundle: SegmentBundleDto,
  ): Promise<PostSegmentResponse>;
  refreshAccessToken(refreshToken: string): Promise<AuthSession>;
}
