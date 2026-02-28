import type {
  TranslationRequest,
  TranslationResponse,
} from "@domain";
import { runtimeConfig } from "../config/runtimeConfig";

interface TranslateApiResponse {
  translatedWord?: unknown;
  translatedText?: unknown;
}

export class TranslationApi {
  async translate(
    accessToken: string,
    request: TranslationRequest,
  ): Promise<TranslationResponse> {
    const translateUrl = this.buildUrl(
      runtimeConfig.apiBaseUrl,
      runtimeConfig.translatePath,
    );

    const response = await fetch(translateUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(this.toBackendPayload(request)),
    });

    const payload = this.parseJsonSafely(await response.text());
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
