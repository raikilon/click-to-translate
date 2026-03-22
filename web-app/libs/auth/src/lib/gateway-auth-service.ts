import { AUTH_LOGIN_PATH, AUTH_LOGOUT_PATH, AUTH_ME_PATH } from './auth-paths';
import type { AuthState } from './auth-state';
import type { CurrentUser } from './current-user';

interface GatewayAuthServiceOptions {
  apiBaseUrl?: string;
}

export class GatewayAuthService {
  constructor(private readonly options: GatewayAuthServiceOptions = {}) {}

  async getAuthState(): Promise<AuthState> {
    const currentUser = await this.getCurrentUser();
    return { isAuthenticated: currentUser !== null };
  }

  async getCurrentUser(): Promise<CurrentUser | null> {
    const response = await fetch(this.buildUrl(AUTH_ME_PATH), {
      method: 'GET',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
      },
    });

    if (response.status === 401) {
      return null;
    }

    if (!response.ok) {
      throw new Error('Current user request failed.');
    }

    const payload = (await response.json()) as Partial<CurrentUser>;
    if (typeof payload.username !== 'string' || payload.username.trim().length === 0) {
      throw new Error('Current user payload is invalid.');
    }

    return { username: payload.username };
  }

  async logout(): Promise<void> {
    const response = await fetch(this.buildUrl(AUTH_LOGOUT_PATH), {
      method: 'POST',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
      },
    });

    if (!response.ok && response.status !== 401) {
      throw new Error('Logout request failed.');
    }
  }

  getLoginUrl(): string {
    return this.buildUrl(AUTH_LOGIN_PATH);
  }

  getGatewayOrigin(): string {
    const apiBaseUrl = this.options.apiBaseUrl?.trim();
    if (!apiBaseUrl) {
      throw new Error('Gateway API base URL is required.');
    }

    try {
      return new URL(apiBaseUrl).origin;
    } catch {
      throw new Error('Gateway API base URL is invalid.');
    }
  }

  private buildUrl(path: string): string {
    const apiBaseUrl = this.options.apiBaseUrl?.trim();
    if (!apiBaseUrl) {
      return path;
    }

    return new URL(path, apiBaseUrl).toString();
  }
}
