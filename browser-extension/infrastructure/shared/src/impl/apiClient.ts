import type {
  ApiClient,
  AuthSession,
  PostSegmentResponse,
  SettingsStore,
} from "@application";
import { normalizeLanguageList } from "@application";
import type { LanguageDto, SegmentBundleDto, SourceMetadataDto } from "@domain";

interface TokenResponseDto {
  access_token?: string;
  refresh_token?: string;
  id_token?: string;
  token_type?: string;
  expires_in?: number;
}

export class ApiHttpError extends Error {
  constructor(
    public readonly status: number,
    message: string,
    public readonly code?: string,
  ) {
    super(message);
    this.name = "ApiHttpError";
  }
}

function buildUrl(base: string, pathOrAbsolute: string): string {
  return new URL(pathOrAbsolute, base).toString();
}

function parseJsonSafely(text: string): unknown {
  if (!text.trim()) {
    return null;
  }

  try {
    return JSON.parse(text) as unknown;
  } catch {
    return text;
  }
}

function sourceMetadataToBackend(metadata?: SourceMetadataDto): unknown {
  if (!metadata) {
    return undefined;
  }

  return {
    type: "generic",
    url: metadata.url,
    domain: metadata.hostname,
    selectionOffset: null,
    paragraphIndex: null,
  };
}

function segmentBundleToBackend(bundle: SegmentBundleDto): unknown {
  return {
    word: bundle.word,
    sentence: bundle.sentence,
    sourceLanguage: bundle.sourceLanguage.code || bundle.sourceLanguage.id,
    targetLanguage: bundle.targetLanguage.code || bundle.targetLanguage.id,
    source: {
      type: bundle.source,
      id: bundle.sourceMetadata?.url ?? bundle.source,
      title: bundle.sourceMetadata?.title ?? bundle.source,
    },
    sourceMetadata: sourceMetadataToBackend(bundle.sourceMetadata),
    occurredAt: bundle.occurredAt,
  };
}

async function readJsonBody(response: Response): Promise<unknown> {
  const rawBody = await response.text();
  return parseJsonSafely(rawBody);
}

function asAuthSession(
  tokenResponse: TokenResponseDto,
  nowMs: number,
): AuthSession {
  if (!tokenResponse.access_token) {
    throw new Error("Token endpoint did not return access_token.");
  }

  const expiresIn =
    typeof tokenResponse.expires_in === "number" ? tokenResponse.expires_in : 3600;

  return {
    accessToken: tokenResponse.access_token,
    refreshToken: tokenResponse.refresh_token,
    idToken: tokenResponse.id_token,
    tokenType: tokenResponse.token_type === "Bearer" ? "Bearer" : undefined,
    expiresAtMs: nowMs + expiresIn * 1000,
  };
}

function toApiErrorMessage(payload: unknown): { message: string; code?: string } {
  if (payload && typeof payload === "object") {
    const object = payload as { message?: unknown; error?: unknown; code?: unknown };
    const messageCandidate =
      typeof object.message === "string"
        ? object.message
        : typeof object.error === "string"
          ? object.error
          : "API request failed.";

    return {
      message: messageCandidate,
      code: typeof object.code === "string" ? object.code : undefined,
    };
  }

  if (typeof payload === "string" && payload.trim()) {
    return { message: payload };
  }

  return { message: "API request failed." };
}

export class HttpApiClient implements ApiClient {
  constructor(private readonly settingsStore: SettingsStore) {}

  async getLanguages(accessToken: string | null): Promise<LanguageDto[]> {
    const settings = await this.settingsStore.get();
    const url = buildUrl(settings.apiBaseUrl, settings.languagesPath);

    const response = await fetch(url, {
      method: "GET",
      headers: {
        Accept: "application/json",
        ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      },
    });

    const payload = await readJsonBody(response);
    if (!response.ok) {
      const error = toApiErrorMessage(payload);
      throw new ApiHttpError(response.status, error.message, error.code);
    }

    return normalizeLanguageList(payload);
  }

  async postSegment(
    accessToken: string,
    bundle: SegmentBundleDto,
  ): Promise<PostSegmentResponse> {
    const settings = await this.settingsStore.get();
    const url = buildUrl(settings.apiBaseUrl, settings.segmentsPath);

    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
        ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
      },
      body: JSON.stringify(segmentBundleToBackend(bundle)),
    });

    const payload = await readJsonBody(response);
    if (!response.ok) {
      const error = toApiErrorMessage(payload);
      throw new ApiHttpError(response.status, error.message, error.code);
    }

    if (typeof payload === "string") {
      return {
        translationText: payload,
      };
    }

    if (!payload || typeof payload !== "object") {
      return {
        translationText: undefined,
      };
    }

    const typedPayload = payload as Partial<PostSegmentResponse>;
    return {
      translationText:
        typeof typedPayload.translationText === "string"
          ? typedPayload.translationText
          : undefined,
      segmentId:
        typeof typedPayload.segmentId === "string" ? typedPayload.segmentId : undefined,
      word: typeof typedPayload.word === "string" ? typedPayload.word : undefined,
      sentence:
        typeof typedPayload.sentence === "string" ? typedPayload.sentence : undefined,
      translatedWord:
        typeof typedPayload.translatedWord === "string"
          ? typedPayload.translatedWord
          : undefined,
      translatedSentence:
        typeof typedPayload.translatedSentence === "string"
          ? typedPayload.translatedSentence
          : undefined,
    };
  }

  async refreshAccessToken(refreshToken: string): Promise<AuthSession> {
    const settings = await this.settingsStore.get();
    if (!settings.authTokenUrl.trim()) {
      throw new Error("Missing authTokenUrl in settings.");
    }

    const body = new URLSearchParams({
      grant_type: "refresh_token",
      refresh_token: refreshToken,
      client_id: settings.oauthClientId,
      scope: settings.scopes.join(" "),
    });

    const response = await fetch(settings.authTokenUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Accept: "application/json",
      },
      body,
    });

    const payload = await readJsonBody(response);
    if (!response.ok) {
      const error = toApiErrorMessage(payload);
      throw new ApiHttpError(response.status, error.message, error.code);
    }

    return asAuthSession(payload as TokenResponseDto, Date.now());
  }
}
