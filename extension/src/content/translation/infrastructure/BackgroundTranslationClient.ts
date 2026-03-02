import type { IBackgroundTranslationClient } from "@/content/translation/application/IBackgroundTranslationClient";
import type { TranslateRequest } from "@/entrypoints/shared/messaging/contracts/TranslateRequest";
import type { TranslateResponse } from "@/entrypoints/shared/messaging/contracts/TranslateResponse";
import { SERVICES, sendBackgroundMessage } from "@/entrypoints/shared/messaging/services";

export class BackgroundTranslationClient implements IBackgroundTranslationClient {
  async translate(word: string, context: string): Promise<string | undefined> {
    const request: TranslateRequest = {
      kind: "translateWord",
      word,
      context,
    };

    const response = await sendBackgroundMessage(SERVICES.translateWord, {
      request,
    });

    return this.toTranslation(response);
  }

  private toTranslation(response: TranslateResponse): string | undefined {
    if (response.kind !== "ok") {
      return undefined;
    }

    const value = response.translation?.trim();
    return value || undefined;
  }
}





