import { Injectable } from '@angular/core';

interface PkceState {
  state: string;
  codeVerifier: string;
}

@Injectable({ providedIn: 'root' })
export class AuthStorageService {
  private readonly pkceKey = 'vocabulary.auth.pkce';
  private readonly redirectKey = 'vocabulary.auth.redirect';

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
