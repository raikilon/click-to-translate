import { Injectable, signal } from '@angular/core';
import { AuthState } from '../domain/auth-state';
import { AuthSession } from '../domain/auth-session';
import { authRuntimeConfig } from '../infrastructure/auth.config';
import { AuthStorageService } from '../infrastructure/auth-storage.service';
import {
  createCodeChallenge,
  generateRandomBase64Url
} from '../infrastructure/pkce.util';
import { KeycloakAuthGateway } from '../infrastructure/keycloak-auth.gateway';

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private static readonly EXPIRY_SKEW_MS = 30_000;

  private readonly session = signal<AuthSession | null>(null);
  private refreshInFlight: Promise<AuthSession | null> | null = null;

  constructor(
    private readonly storage: AuthStorageService,
    private readonly authGateway: KeycloakAuthGateway
  ) {}

  getAuthState(): AuthState {
    return {
      isAuthenticated: this.isAuthenticated()
    };
  }

  isAuthenticated(): boolean {
    const current = this.session();
    if (!current) {
      return false;
    }

    return this.isSessionUsable(current) || this.isRefreshUsable(current);
  }

  async getAccessToken(): Promise<string | null> {
    const session = await this.ensureSession();
    if (!session) {
      return null;
    }

    return session.accessToken;
  }

  async beginLogin(redirectUrl?: string): Promise<void> {
    if (redirectUrl && redirectUrl.trim().length > 0) {
      this.storage.writePostLoginRedirect(redirectUrl);
    }

    const state = generateRandomBase64Url(24);
    const codeVerifier = generateRandomBase64Url(48);
    const codeChallenge = await createCodeChallenge(codeVerifier);

    this.storage.writePkceState({ state, codeVerifier });

    const authorizationUrl = this.authGateway.createAuthorizationUrl({
      state,
      codeChallenge,
      redirectUri: authRuntimeConfig.redirectUri
    });

    window.location.assign(authorizationUrl);
  }

  async completeLogin(callbackUrl: string): Promise<void> {
    const callback = new URL(callbackUrl);
    const oauthError = callback.searchParams.get('error');
    if (oauthError) {
      throw new Error(oauthError);
    }

    const code = callback.searchParams.get('code');
    const state = callback.searchParams.get('state');
    if (!code || !state) {
      throw new Error('Missing login callback parameters.');
    }

    const pkceState = this.storage.readPkceState();
    if (!pkceState || pkceState.state !== state) {
      throw new Error('Invalid login state.');
    }

    const session = await this.authGateway.exchangeAuthorizationCode({
      code,
      codeVerifier: pkceState.codeVerifier,
      redirectUri: authRuntimeConfig.redirectUri
    });

    this.session.set(session);
    this.storage.clearPkceState();
  }

  consumePostLoginRedirect(): string | null {
    return this.storage.consumePostLoginRedirect();
  }

  logout(): void {
    this.session.set(null);
    this.storage.clearPkceState();
    this.refreshInFlight = null;
  }

  private async ensureSession(): Promise<AuthSession | null> {
    const current = this.session();
    if (!current) {
      return null;
    }

    if (this.isSessionUsable(current)) {
      return current;
    }

    if (!this.isRefreshUsable(current)) {
      this.logout();
      return null;
    }

    return this.refreshSession(current.refreshToken);
  }

  private isSessionUsable(session: AuthSession): boolean {
    return (
      session.accessTokenExpiresAtMs - AuthSessionService.EXPIRY_SKEW_MS >
      Date.now()
    );
  }

  private isRefreshUsable(session: AuthSession): boolean {
    return (
      session.refreshToken.trim().length > 0 &&
      session.refreshTokenExpiresAtMs - AuthSessionService.EXPIRY_SKEW_MS >
        Date.now()
    );
  }

  private refreshSession(refreshToken: string): Promise<AuthSession | null> {
    if (this.refreshInFlight) {
      return this.refreshInFlight;
    }

    this.refreshInFlight = this.authGateway
      .refreshAccessToken(refreshToken)
      .then((session) => {
        this.session.set(session);
        return session;
      })
      .catch(() => {
        this.logout();
        return null;
      })
      .finally(() => {
        this.refreshInFlight = null;
      });

    return this.refreshInFlight;
  }
}
