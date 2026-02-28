import type { AuthState, AuthStateProvider } from "@application";
import { runtimeConfig } from "../config/runtimeConfig";
import { AuthRequiredError, AuthSessionExpiredError } from "./AuthErrors";
import { extensionIdentity } from "./identity";
import { authLogoutStorageItem } from "../storage/items";

export interface AuthSession {
  accessToken: string;
  idToken?: string;
  tokenType?: "Bearer";
  expiresAtMs: number;
}

interface AuthorizationServer {
  issuer: string;
  authorization_endpoint: string;
  token_endpoint: string;
}

interface OAuthClient {
  client_id: string;
}

interface TokenEndpointResponse {
  access_token: string;
  token_type: string;
  expires_in?: number;
  id_token?: string;
}

export class AuthSessionManager implements AuthStateProvider {
  private static readonly EXPIRY_SKEW_MS = 30_000;
  private session: AuthSession | null = null;

  async getAuthState(): Promise<AuthState> {
    try {
      const session = await this.ensureSession(false);
      if (!session) {
        return { isLoggedIn: false };
      }

      return {
        isLoggedIn: true,
        expiresAtMs: session.expiresAtMs,
      };
    } catch (error) {
      if (
        error instanceof AuthRequiredError ||
        error instanceof AuthSessionExpiredError
      ) {
        return { isLoggedIn: false };
      }

      throw error;
    }
  }

  async login(): Promise<AuthState> {
    const session = await this.ensureSession(true);
    if (!session) {
      return { isLoggedIn: false };
    }

    await authLogoutStorageItem.setValue(false);

    return {
      isLoggedIn: true,
      expiresAtMs: session.expiresAtMs,
    };
  }

  async logout(): Promise<void> {
    this.session = null;
    await authLogoutStorageItem.setValue(true);
  }

  async getAccessToken(interactive = false): Promise<string | null> {
    const session = await this.ensureSession(interactive);
    if (!session) {
      return null;
    }

    return session.accessToken;
  }

  private async ensureSession(interactive: boolean): Promise<AuthSession | null> {
    const isLoggedOut = await authLogoutStorageItem.getValue();
    if (!interactive && isLoggedOut) {
      this.session = null;
      return null;
    }

    if (this.session && this.isSessionUsable(this.session)) {
      return this.session;
    }

    const hadSession = this.session !== null;
    this.session = null;

    const silentSession = await this.tryLoginByAuthorizationCode(false);
    if (silentSession) {
      this.session = silentSession;
      return silentSession;
    }

    if (!interactive) {
      if (hadSession) {
        throw new AuthSessionExpiredError();
      }

      return null;
    }

    const interactiveSession = await this.tryLoginByAuthorizationCode(true);
    if (!interactiveSession) {
      throw new AuthRequiredError();
    }

    this.session = interactiveSession;
    return interactiveSession;
  }

  private isSessionUsable(session: AuthSession): boolean {
    return session.expiresAtMs - AuthSessionManager.EXPIRY_SKEW_MS > Date.now();
  }

  private async loginByAuthorizationCode(interactive: boolean): Promise<AuthSession> {
    this.validateAuthConfig();

    const codeVerifier = this.generateRandomBase64Url(48);
    const state = this.generateRandomBase64Url(24);
    const codeChallenge = await this.calculatePkceCodeChallenge(codeVerifier);
    const redirectUri = extensionIdentity.getRedirectUrl("oauth2");
    const client = this.createClient();

    const authorizeUrl = new URL(runtimeConfig.authAuthorizeUrl);
    authorizeUrl.searchParams.set("response_type", "code");
    authorizeUrl.searchParams.set("client_id", client.client_id);
    authorizeUrl.searchParams.set("redirect_uri", redirectUri);
    authorizeUrl.searchParams.set("state", state);
    authorizeUrl.searchParams.set("code_challenge", codeChallenge);
    authorizeUrl.searchParams.set("code_challenge_method", "S256");
    if (runtimeConfig.scopes.length > 0) {
      authorizeUrl.searchParams.set("scope", runtimeConfig.scopes.join(" "));
    }

    const redirectUrl = await extensionIdentity.launchWebAuthFlow(
      authorizeUrl.toString(),
      interactive,
    );
    const redirect = new URL(redirectUrl);
    const as = this.createAuthorizationServer(
      redirect.searchParams.get("iss") ?? undefined,
    );
    const code = this.validateAuthResponse(as, redirect, state);
    const tokenResponse = await this.requestAuthorizationCodeGrant({
      authorizationServer: as,
      client,
      code,
      redirectUri,
      codeVerifier,
    });

    return this.asAuthSession(tokenResponse, Date.now());
  }

  private async tryLoginByAuthorizationCode(
    interactive: boolean,
  ): Promise<AuthSession | null> {
    try {
      return await this.loginByAuthorizationCode(interactive);
    } catch (error) {
      if (!interactive && this.isSilentAuthUnavailableError(error)) {
        return null;
      }

      throw error;
    }
  }

  private validateAuthConfig(): void {
    if (!runtimeConfig.authAuthorizeUrl.trim()) {
      throw new Error("Missing auth authorize URL configuration.");
    }

    if (!runtimeConfig.authTokenUrl.trim()) {
      throw new Error("Missing auth token URL configuration.");
    }

    if (!runtimeConfig.oauthClientId.trim()) {
      throw new Error("Missing OAuth client ID configuration.");
    }
  }

  private inferIssuer(authorizeUrl: string): string {
    const parsedAuthorizeUrl = new URL(authorizeUrl);
    const keycloakSuffix = "/protocol/openid-connect/auth";
    if (parsedAuthorizeUrl.pathname.endsWith(keycloakSuffix)) {
      parsedAuthorizeUrl.pathname = parsedAuthorizeUrl.pathname.slice(
        0,
        -keycloakSuffix.length,
      );
      parsedAuthorizeUrl.search = "";
      parsedAuthorizeUrl.hash = "";
      return parsedAuthorizeUrl.toString();
    }

    return parsedAuthorizeUrl.origin;
  }

  private createAuthorizationServer(
    issuerOverride?: string,
  ): AuthorizationServer {
    return {
      issuer: issuerOverride ?? this.inferIssuer(runtimeConfig.authAuthorizeUrl),
      authorization_endpoint: runtimeConfig.authAuthorizeUrl,
      token_endpoint: runtimeConfig.authTokenUrl,
    };
  }

  private createClient(): OAuthClient {
    return {
      client_id: runtimeConfig.oauthClientId,
    };
  }

  private validateAuthResponse(
    _authorizationServer: AuthorizationServer,
    redirect: URL,
    expectedState: string,
  ): string {
    const returnedState = redirect.searchParams.get("state");
    if (returnedState !== expectedState) {
      throw new Error("OAuth state mismatch.");
    }

    const oauthError = redirect.searchParams.get("error");
    if (oauthError) {
      const errorDescription = redirect.searchParams.get("error_description");
      throw new Error(
        errorDescription ? `${oauthError}: ${errorDescription}` : oauthError,
      );
    }

    const code = redirect.searchParams.get("code");
    if (!code) {
      throw new Error("Missing authorization code in redirect URL.");
    }

    return code;
  }

  private async requestAuthorizationCodeGrant(input: {
    authorizationServer: AuthorizationServer;
    client: OAuthClient;
    code: string;
    redirectUri: string;
    codeVerifier: string;
  }): Promise<TokenEndpointResponse> {
    const response = await fetch(input.authorizationServer.token_endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Accept: "application/json",
      },
      body: new URLSearchParams({
        grant_type: "authorization_code",
        client_id: input.client.client_id,
        code: input.code,
        redirect_uri: input.redirectUri,
        code_verifier: input.codeVerifier,
      }).toString(),
    });

    const payload = await this.parseJsonSafely(await response.text());
    if (!response.ok) {
      throw new Error(this.parseOAuthError(payload));
    }

    if (!this.isTokenEndpointResponse(payload)) {
      throw new Error("Invalid token response from OAuth provider.");
    }

    return payload;
  }

  private async calculatePkceCodeChallenge(codeVerifier: string): Promise<string> {
    const digest = await crypto.subtle.digest(
      "SHA-256",
      new TextEncoder().encode(codeVerifier),
    );
    return this.base64UrlEncode(new Uint8Array(digest));
  }

  private generateRandomBase64Url(byteLength: number): string {
    const bytes = new Uint8Array(byteLength);
    crypto.getRandomValues(bytes);
    return this.base64UrlEncode(bytes);
  }

  private base64UrlEncode(bytes: Uint8Array): string {
    let binary = "";
    for (const byte of bytes) {
      binary += String.fromCharCode(byte);
    }

    return btoa(binary).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/g, "");
  }

  private async parseJsonSafely(rawText: string): Promise<unknown> {
    if (!rawText.trim()) {
      return null;
    }

    try {
      return JSON.parse(rawText) as unknown;
    } catch {
      return rawText;
    }
  }

  private parseOAuthError(payload: unknown): string {
    if (payload && typeof payload === "object") {
      const value = payload as { error?: unknown; error_description?: unknown };
      if (typeof value.error_description === "string" && value.error_description.trim()) {
        return value.error_description;
      }

      if (typeof value.error === "string" && value.error.trim()) {
        return value.error;
      }
    }

    if (typeof payload === "string" && payload.trim()) {
      return payload;
    }

    return "OAuth token request failed.";
  }

  private isTokenEndpointResponse(payload: unknown): payload is TokenEndpointResponse {
    return !!(
      payload &&
      typeof payload === "object" &&
      typeof (payload as { access_token?: unknown }).access_token === "string" &&
      typeof (payload as { token_type?: unknown }).token_type === "string"
    );
  }

  private isSilentAuthUnavailableError(error: unknown): boolean {
    if (!(error instanceof Error)) {
      return false;
    }

    const message = error.message.toLowerCase();
    return (
      message.includes("interaction") ||
      message.includes("not signed in") ||
      message.includes("login required") ||
      message.includes("access denied") ||
      message.includes("oauth2 not granted") ||
      message.includes("user did not approve")
    );
  }

  private asAuthSession(
    tokenResponse: TokenEndpointResponse,
    nowMs: number,
  ): AuthSession {
    const expiresInSeconds =
      typeof tokenResponse.expires_in === "number"
        ? tokenResponse.expires_in
        : 3600;

    return {
      accessToken: tokenResponse.access_token,
      idToken: tokenResponse.id_token,
      tokenType:
        tokenResponse.token_type.toLowerCase() === "bearer"
          ? "Bearer"
          : undefined,
      expiresAtMs: nowMs + expiresInSeconds * 1000,
    };
  }
}
