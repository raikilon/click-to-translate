import { Injectable } from '@angular/core';
import { AuthSession } from '../domain/auth-session';

interface PkceState {
  state: string;
  codeVerifier: string;
}

@Injectable({ providedIn: 'root' })
export class AuthStorageService {
  private readonly sessionKey = 'vocabulary.auth.session';
  private readonly pkceKey = 'vocabulary.auth.pkce';
  private readonly redirectKey = 'vocabulary.auth.redirect';

  readSession(): AuthSession | null {
    const raw = localStorage.getItem(this.sessionKey);
    if (!raw) {
      return null;
    }

    try {
      const value = JSON.parse(raw) as {
        accessToken?: unknown;
        expiresAtMs?: unknown;
      };
      if (
        typeof value.accessToken !== 'string' ||
        typeof value.expiresAtMs !== 'number'
      ) {
        return null;
      }

      return {
        accessToken: value.accessToken,
        expiresAtMs: value.expiresAtMs
      };
    } catch {
      return null;
    }
  }

  writeSession(session: AuthSession): void {
    localStorage.setItem(this.sessionKey, JSON.stringify(session));
  }

  clearSession(): void {
    localStorage.removeItem(this.sessionKey);
  }

  writePkceState(state: PkceState): void {
    sessionStorage.setItem(this.pkceKey, JSON.stringify(state));
  }

  readPkceState(): PkceState | null {
    const raw = sessionStorage.getItem(this.pkceKey);
    if (!raw) {
      return null;
    }

    try {
      const value = JSON.parse(raw) as {
        state?: unknown;
        codeVerifier?: unknown;
      };
      if (typeof value.state !== 'string' || typeof value.codeVerifier !== 'string') {
        return null;
      }

      return {
        state: value.state,
        codeVerifier: value.codeVerifier
      };
    } catch {
      return null;
    }
  }

  clearPkceState(): void {
    sessionStorage.removeItem(this.pkceKey);
  }

  writePostLoginRedirect(url: string): void {
    sessionStorage.setItem(this.redirectKey, url);
  }

  consumePostLoginRedirect(): string | null {
    const value = sessionStorage.getItem(this.redirectKey);
    sessionStorage.removeItem(this.redirectKey);
    return value;
  }
}
