import type { AuthFlow, AuthSession, Clock, SettingsStore } from "@application";
import type { IdentityPort } from "../platform/BrowserAdapter";
import { codeChallengeS256, generateCodeVerifier, generateState } from "./pkce";

interface TokenResponseDto {
  access_token?: string;
  refresh_token?: string;
  id_token?: string;
  token_type?: string;
  expires_in?: number;
  error?: string;
  error_description?: string;
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

function toAuthSession(payload: TokenResponseDto, nowMs: number): AuthSession {
  if (!payload.access_token) {
    throw new Error("Token response missing access_token.");
  }

  const expiresInSeconds =
    typeof payload.expires_in === "number" ? payload.expires_in : 3600;

  return {
    accessToken: payload.access_token,
    refreshToken: payload.refresh_token,
    idToken: payload.id_token,
    tokenType: payload.token_type === "Bearer" ? "Bearer" : undefined,
    expiresAtMs: nowMs + expiresInSeconds * 1000,
  };
}

export class ExtensionAuthFlow implements AuthFlow {
  constructor(
    private readonly settingsStore: SettingsStore,
    private readonly clock: Clock,
    private readonly identity: IdentityPort,
  ) {}

  async loginInteractive(): Promise<AuthSession> {
    const settings = await this.settingsStore.get();
    if (!settings.authAuthorizeUrl.trim()) {
      throw new Error("Missing authAuthorizeUrl in settings.");
    }
    if (!settings.authTokenUrl.trim()) {
      throw new Error("Missing authTokenUrl in settings.");
    }
    if (!settings.oauthClientId.trim()) {
      throw new Error("Missing oauthClientId in settings.");
    }

    const codeVerifier = generateCodeVerifier();
    const state = generateState();
    const codeChallenge = await codeChallengeS256(codeVerifier);
    const redirectUri = this.identity.getRedirectUrl("oauth2");

    const authorizeUrl = new URL(settings.authAuthorizeUrl);
    authorizeUrl.searchParams.set("response_type", "code");
    authorizeUrl.searchParams.set("client_id", settings.oauthClientId);
    authorizeUrl.searchParams.set("redirect_uri", redirectUri);
    authorizeUrl.searchParams.set("state", state);
    authorizeUrl.searchParams.set("code_challenge", codeChallenge);
    authorizeUrl.searchParams.set("code_challenge_method", "S256");

    const requestedScopes = settings.scopes.filter((scope) => scope.trim());
    if (requestedScopes.length > 0) {
      authorizeUrl.searchParams.set("scope", requestedScopes.join(" "));
    }

    const redirectUrl = await this.identity.launchWebAuthFlow(authorizeUrl.toString());
    const redirect = new URL(redirectUrl);

    const error = redirect.searchParams.get("error");
    if (error) {
      const description = redirect.searchParams.get("error_description");
      throw new Error(description ? `${error}: ${description}` : error);
    }

    const returnedState = redirect.searchParams.get("state");
    if (!returnedState || returnedState !== state) {
      throw new Error("OAuth state mismatch.");
    }

    const code = redirect.searchParams.get("code");
    if (!code) {
      throw new Error("OAuth authorization code missing.");
    }

    const tokenBody = new URLSearchParams({
      grant_type: "authorization_code",
      code,
      client_id: settings.oauthClientId,
      redirect_uri: redirectUri,
      code_verifier: codeVerifier,
    });

    const response = await fetch(settings.authTokenUrl, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Accept: "application/json",
      },
      body: tokenBody,
    });

    const rawBody = await response.text();
    const payload = parseJsonSafely(rawBody) as TokenResponseDto;
    if (!response.ok) {
      const errorMessage =
        typeof payload?.error_description === "string"
          ? payload.error_description
          : typeof payload?.error === "string"
            ? payload.error
            : "Token exchange failed.";
      throw new Error(errorMessage);
    }

    return toAuthSession(payload, this.clock.nowMs());
  }
}
