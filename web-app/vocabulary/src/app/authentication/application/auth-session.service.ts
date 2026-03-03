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
  private readonly session = signal<AuthSession | null>(null);

  constructor(
    private readonly storage: AuthStorageService,
    private readonly authGateway: KeycloakAuthGateway
  ) {
    this.session.set(this.storage.readSession());
    this.clearIfExpired();
  }

  getAuthState(): AuthState {
    return { isAuthenticated: this.isAuthenticated() };
  }

  isAuthenticated(): boolean {
    return this.getValidSession() !== null;
  }

  accessToken(): string | null {
    return this.getValidSession()?.accessToken ?? null;
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
    this.storage.writeSession(session);
    this.storage.clearPkceState();
  }

  consumePostLoginRedirect(): string | null {
    return this.storage.consumePostLoginRedirect();
  }

  logout(): void {
    this.session.set(null);
    this.storage.clearSession();
    this.storage.clearPkceState();
  }

  private clearIfExpired(): void {
    const current = this.session();
    if (current && current.expiresAtMs <= Date.now()) {
      this.logout();
    }
  }

  private getValidSession(): AuthSession | null {
    this.clearIfExpired();
    return this.session();
  }
}
