import type { ITranslationGateway } from "@application";
import type { TranslationRequest, TranslationResponse } from "@domain";
import { TranslationApi } from "./api/TranslationApi";
import { AuthSessionManager } from "./auth/AuthSessionManager";
import { AuthRequiredError } from "./auth/AuthErrors";

export class TranslationGateway implements ITranslationGateway {
  constructor(
    private readonly translationApi: TranslationApi,
    private readonly authSessionManager: AuthSessionManager,
  ) {}

  async translate(request: TranslationRequest): Promise<TranslationResponse> {
    const accessToken = await this.authSessionManager.getAccessToken(false);
    if (!accessToken) {
      throw new AuthRequiredError();
    }

    return this.translationApi.translate(accessToken, request);
  }
}
