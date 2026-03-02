import type {
  TranslationRequest,
  TranslationResponse,
} from "@/content/translation/domain/Translation";

export interface ITranslationGateway {
  translate(request: TranslationRequest): Promise<TranslationResponse>;
}





