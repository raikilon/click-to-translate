import type {
  TranslationRequest,
  TranslationResponse,
} from "@/content/translation/domain/Translation";
import { AuthRequiredError } from "@/content/authentication/application/AuthErrors";
import { translationRuntimeConfig } from "./TranslationRuntimeConfig";

interface TranslateApiResponse {
  translatedWord?: unknown;
  translatedText?: unknown;
}

export class TranslationApi {
  async listLanguages(): Promise<string[]> {
    const languagesUrl = this.buildUrl(
      translationRuntimeConfig.apiBaseUrl,
      translationRuntimeConfig.translateLanguagesPath,
    );

    const response = await fetch(languagesUrl, {
      method: "GET",
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    const payload = this.parseJsonSafely(await response.text());
    if (response.status === 401) {
      throw new AuthRequiredError();
    }

    if (!response.ok) {
      throw new Error(
        this.parseErrorMessage(payload, "Translation languages request failed."),
      );
    }

    if (!Array.isArray(payload)) {
      return [];
    }

    return payload
      .filter((value): value is string => typeof value === "string")
      .map((value) => value.trim())
      .filter(Boolean);
  }

  async translate(
    request: TranslationRequest,
  ): Promise<TranslationResponse> {
    const translateUrl = this.buildUrl(
      translationRuntimeConfig.apiBaseUrl,
      translationRuntimeConfig.segmentPath,
    );

    const response = await fetch(translateUrl, {
      method: "POST",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      body: JSON.stringify(this.toBackendPayload(request)),
    });

    const payload = this.parseJsonSafely(await response.text());
    if (response.status === 401) {
      throw new AuthRequiredError();
    }

    if (!response.ok) {
      throw new Error(
        this.parseErrorMessage(payload, "Translation API request failed."),
      );
    }

    if (typeof payload === "string") {
      return { translatedText: payload };
    }

    if (!payload || typeof payload !== "object") {
      return {};
    }

    const typed = payload as TranslateApiResponse;
    if (typeof typed.translatedText === "string") {
      return { translatedText: typed.translatedText };
    }

    if (typeof typed.translatedWord === "string") {
      return { translatedText: typed.translatedWord };
    }

    return {};
  }

  private buildUrl(base: string, pathOrAbsolute: string): string {
    return new URL(pathOrAbsolute, base).toString();
  }

  private parseJsonSafely(rawText: string): unknown {
    if (!rawText.trim()) {
      return null;
    }

    try {
      return JSON.parse(rawText) as unknown;
    } catch {
      return rawText;
    }
  }

  private parseErrorMessage(payload: unknown, fallback: string): string {
    if (payload && typeof payload === "object") {
      const value = payload as { message?: unknown; error?: unknown };
      if (typeof value.message === "string") {
        return value.message;
      }

      if (typeof value.error === "string") {
        return value.error;
      }
    }

    if (typeof payload === "string" && payload.trim()) {
      return payload;
    }

    return fallback;
  }

  private toBackendPayload(request: TranslationRequest): unknown {
    return {
      word: request.selectedWord,
      sentence: request.textAround,
      sourceLanguage: request.sourceLanguageId,
      targetLanguage: request.targetLanguageId,
    };
  }
}





