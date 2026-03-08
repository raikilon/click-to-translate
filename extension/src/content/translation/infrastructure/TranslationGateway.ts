import type { ITranslationGateway } from "@/content/translation/application/TranslationGateway";
import type { TranslationRequest, TranslationResponse } from "@/content/translation/domain/Translation";
import { TranslationApi } from "./TranslationApi";

export class TranslationGateway implements ITranslationGateway {
  constructor(private readonly translationApi: TranslationApi) {}

  async translate(request: TranslationRequest): Promise<TranslationResponse> {
    return this.translationApi.translate(request);
  }
}





