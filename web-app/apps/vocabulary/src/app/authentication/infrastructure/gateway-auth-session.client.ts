import { Injectable } from '@angular/core';
import { AuthState } from '../domain/auth-state';

@Injectable({ providedIn: 'root' })
export class GatewayAuthSessionClient {
  async getAuthState(): Promise<AuthState> {
    const response = await fetch('/auth/me', {
      method: 'GET',
      credentials: 'include',
      headers: {
        Accept: 'application/json',
      },
    });

    if (response.status === 401) {
      return { isAuthenticated: false };
    }

    if (!response.ok) {
      throw new Error('Current user request failed.');
    }

    return { isAuthenticated: true };
  }

  async logout(): Promise<void> {
    const response = await fetch('/auth/logout', {
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
}
