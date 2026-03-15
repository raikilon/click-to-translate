import { Injectable } from '@angular/core';
import { GatewayAuthSessionClient } from '../infrastructure/gateway-auth-session.client';
import { AuthState } from '../domain/auth-state';

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private static readonly LOGIN_POLL_INTERVAL_MS = 500;
  private static readonly LOGIN_TIMEOUT_MS = 180_000;

  private authStateCache: AuthState | null = null;
  private authStateInFlight: Promise<AuthState> | null = null;

  constructor(private readonly authSessionClient: GatewayAuthSessionClient) {}

  async getAuthState(): Promise<AuthState> {
    if (this.authStateCache) {
      return this.authStateCache;
    }

    if (this.authStateInFlight) {
      return this.authStateInFlight;
    }

    this.authStateInFlight = this.authSessionClient
      .getAuthState()
      .then((authState) => {
        this.authStateCache = authState;
        return authState;
      })
      .finally(() => {
        this.authStateInFlight = null;
      });

    return this.authStateInFlight;
  }

  async isAuthenticated(): Promise<boolean> {
    const authState = await this.getAuthState();
    return authState.isAuthenticated;
  }

  clearAuthState(): void {
    this.authStateCache = null;
    this.authStateInFlight = null;
  }

  async beginLogin(): Promise<void> {
    this.clearAuthState();
    const loginPopup = window.open('/auth/login', '_blank', 'popup=yes,width=540,height=760');
    if (!loginPopup) {
      throw new Error('Login popup was blocked. Please allow popups and try again.');
    }

    try {
      await this.waitUntilAuthenticated(loginPopup);
    } finally {
      if (!loginPopup.closed) {
        loginPopup.close();
      }
    }
  }

  async logout(): Promise<void> {
    await this.authSessionClient.logout();

    this.clearAuthState();
    this.authStateCache = { isAuthenticated: false };
  }

  private async waitUntilAuthenticated(loginPopup: Window): Promise<void> {
    const timeoutAt = Date.now() + AuthSessionService.LOGIN_TIMEOUT_MS;
    while (Date.now() < timeoutAt) {
      if (loginPopup.closed) {
        this.authStateCache = null;
        throw new Error('Login was cancelled before completion.');
      }

      const authState = await this.fetchAuthStateForLoginPolling();
      if (authState.isAuthenticated) {
        this.authStateCache = authState;
        return;
      }

      await this.delay(AuthSessionService.LOGIN_POLL_INTERVAL_MS);
    }

    this.authStateCache = null;
    throw new Error('Login timed out. Please try again.');
  }

  private async fetchAuthStateForLoginPolling(): Promise<AuthState> {
    try {
      return await this.authSessionClient.getAuthState();
    } catch {
      return { isAuthenticated: false };
    }
  }

  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => {
      window.setTimeout(resolve, ms);
    });
  }
}
