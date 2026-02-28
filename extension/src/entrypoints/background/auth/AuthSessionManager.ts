import type { AuthState, AuthStateProvider } from "@application";
import * as oauth from "oauth4webapi";
import { runtimeConfig } from "../config/runtimeConfig";
import { extensionIdentity } from "./identity";

export interface AuthSession {
  accessToken: string;
  idToken?: string;
  tokenType?: "Bearer";
  expiresAtMs: number;
}

export class AuthSessionManager implements AuthStateProvider {
  private static readonly EXPIRY_SKEW_MS = 30_000;
  private session: AuthSession | null = null;

  async getAuthState(): Promise<AuthState> {
    const session = await this.ensureSession(false);
    if (!session) {
      return { isLoggedIn: false };
    }

    return {
      isLoggedIn: true,
      expiresAtMs: session.expiresAtMs,
    };
  }

  async login(): Promise<AuthState> {
    const session = await this.ensureSession(true);
    if (!session) {
      return { isLoggedIn: false };
    }

    return {
      isLoggedIn: true,
      expiresAtMs: session.expiresAtMs,
    };
  }

  async logout(): Promise<void> {
    this.session = null;
  }

  async getAccessToken(interactive = false): Promise<string | null> {
    const session = await this.ensureSession(interactive);
    return session?.accessToken ?? null;
  }

  private async ensureSession(interactive: boolean): Promise<AuthSession | null> {
    if (this.session && this.isSessionUsable(this.session)) {
      return this.session;
    }

    this.session = null;

    // Attempt silent PKCE first to reuse existing IdP session without UI prompts.
    try {
      const silentSession = await this.loginByAuthorizationCode(false);
      this.session = silentSession;
      return silentSession;
    } catch {
      // Silent auth can fail when the IdP session is absent/expired.
    }

    if (!interactive) {
      return null;
    }

    const interactiveSession = await this.loginByAuthorizationCode(true);
    this.session = interactiveSession;
    return interactiveSession;
  }

  private isSessionUsable(session: AuthSession): boolean {
    return session.expiresAtMs - AuthSessionManager.EXPIRY_SKEW_MS > Date.now();
  }

  private async loginByAuthorizationCode(interactive: boolean): Promise<AuthSession> {
    this.validateAuthConfig();

    const codeVerifier = oauth.generateRandomCodeVerifier();
    const state = oauth.generateRandomState();
    const codeChallenge = await oauth.calculatePKCECodeChallenge(codeVerifier);
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
    const authResponse = oauth.validateAuthResponse(as, client, redirect, state);
    const codeGrantResponse = await oauth.authorizationCodeGrantRequest(
      as,
      client,
      oauth.None(),
      authResponse,
      redirectUri,
      codeVerifier,
      this.tokenEndpointRequestOptions(),
    );
    const tokenResponse = await oauth.processAuthorizationCodeResponse(
      as,
      client,
      codeGrantResponse,
    );

    return this.asAuthSession(tokenResponse, Date.now());
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
  ): oauth.AuthorizationServer {
    return {
      issuer: issuerOverride ?? this.inferIssuer(runtimeConfig.authAuthorizeUrl),
      authorization_endpoint: runtimeConfig.authAuthorizeUrl,
      token_endpoint: runtimeConfig.authTokenUrl,
    };
  }

  private createClient(): oauth.Client {
    return {
      client_id: runtimeConfig.oauthClientId,
    };
  }

  private shouldAllowInsecureRequests(): boolean {
    try {
      const authUrl = new URL(runtimeConfig.authAuthorizeUrl);
      const tokenUrl = new URL(runtimeConfig.authTokenUrl);
      return authUrl.protocol === "http:" || tokenUrl.protocol === "http:";
    } catch {
      return false;
    }
  }

  private tokenEndpointRequestOptions(): oauth.TokenEndpointRequestOptions | undefined {
    if (!this.shouldAllowInsecureRequests()) {
      return undefined;
    }

    return {
      [oauth.allowInsecureRequests]: true,
    };
  }

  private asAuthSession(
    tokenResponse: oauth.TokenEndpointResponse,
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
