import type { ITranslationGateway } from "@/content/translation/application/TranslationGateway";
import type { TranslationRequest, TranslationResponse } from "@/content/translation/domain/Translation";
import { TranslationApi } from "./TranslationApi";
import type { IAccessTokenProvider } from "@/content/authentication/application/IAccessTokenProvider";
import { AuthRequiredError } from "@/content/authentication/application/AuthErrors";

export class TranslationGateway implements ITranslationGateway {
  constructor(
    private readonly translationApi: TranslationApi,
    private readonly accessTokenProvider: IAccessTokenProvider,
  ) {}

  async translate(request: TranslationRequest): Promise<TranslationResponse> {
    const accessToken = await this.accessTokenProvider.getAccessToken(false);
    if (!accessToken) {
      throw new AuthRequiredError();
    }

    return this.translationApi.translate(accessToken, request);
  }
}





