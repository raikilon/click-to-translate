import { Injectable } from '@angular/core';
import {
  AUTH_LOGIN_PATH,
  GatewayAuthService,
  GetAuthStateUseCase,
  type AuthState,
  LogoutUseCase,
} from '@vocabulary/auth';

@Injectable({ providedIn: 'root' })
export class AuthSessionService {
  private static readonly LOGIN_POLL_INTERVAL_MS = 500;
  private static readonly LOGIN_TIMEOUT_MS = 180_000;

  private readonly getAuthStateUseCase: GetAuthStateUseCase;
  private readonly logoutUseCase: LogoutUseCase;

  constructor() {
    const gatewayAuthService = new GatewayAuthService();
    this.getAuthStateUseCase = new GetAuthStateUseCase(gatewayAuthService);
    this.logoutUseCase = new LogoutUseCase(gatewayAuthService);
  }

  async getAuthState(): Promise<AuthState> {
    return this.getAuthStateUseCase.execute();
  }

  async isAuthenticated(): Promise<boolean> {
    const authState = await this.getAuthState();
    return authState.isAuthenticated;
  }

  clearAuthState(): void {
    return;
  }

  async beginLogin(): Promise<void> {
    const loginPopup = window.open(AUTH_LOGIN_PATH, '_blank', 'popup=yes,width=540,height=760');
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
    await this.logoutUseCase.execute();
  }

  private async waitUntilAuthenticated(loginPopup: Window): Promise<void> {
    const timeoutAt = Date.now() + AuthSessionService.LOGIN_TIMEOUT_MS;
    while (Date.now() < timeoutAt) {
      if (loginPopup.closed) {
        throw new Error('Login was cancelled before completion.');
      }

      const authState = await this.fetchAuthStateForLoginPolling();
      if (authState.isAuthenticated) {
        return;
      }

      await this.delay(AuthSessionService.LOGIN_POLL_INTERVAL_MS);
    }

    throw new Error('Login timed out. Please try again.');
  }

  private async fetchAuthStateForLoginPolling(): Promise<AuthState> {
    try {
      return await this.getAuthStateUseCase.execute();
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
