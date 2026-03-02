import type { GetAccessTokenUseCase } from "@/content/authentication/application/GetAccessTokenUseCase";
import { AuthRequiredError } from "@/content/authentication/application/AuthErrors";
import type { ITranslationLanguagesGateway } from "./ITranslationLanguagesGateway";

export class ListTranslationLanguagesUseCase {
  constructor(
    private readonly getAccessTokenUseCase: GetAccessTokenUseCase,
    private readonly translationLanguagesGateway: ITranslationLanguagesGateway,
  ) {}

  async execute(): Promise<string[]> {
    const accessToken = await this.getAccessTokenUseCase.execute(false);
    if (!accessToken) {
      throw new AuthRequiredError();
    }

    return this.translationLanguagesGateway.listLanguages(accessToken);
  }
}
