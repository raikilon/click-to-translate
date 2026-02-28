import type {
  TranslationRequest,
  TranslationResponse,
} from "@domain";

export interface ITranslationGateway {
  translate(request: TranslationRequest): Promise<TranslationResponse>;
}
