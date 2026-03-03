import { Injectable } from '@angular/core';
import {
  AuthGateway,
  AuthorizationCodeExchangeRequest
} from '../application/auth-gateway';
import { AuthSession } from '../domain/auth-session';
import { authRuntimeConfig } from './auth.config';

interface TokenEndpointResponse {
  access_token: string;
  token_type: string;
  expires_in?: number;
}

@Injectable({ providedIn: 'root' })
export class KeycloakAuthGateway implements AuthGateway {
  createAuthorizationUrl(input: {
    state: string;
    codeChallenge: string;
    redirectUri: string;
  }): string {
    const url = new URL(authRuntimeConfig.authorizationEndpoint);
    url.searchParams.set('response_type', 'code');
    url.searchParams.set('client_id', authRuntimeConfig.clientId);
    url.searchParams.set('redirect_uri', input.redirectUri);
    url.searchParams.set('scope', authRuntimeConfig.scopes.join(' '));
    url.searchParams.set('state', input.state);
    url.searchParams.set('code_challenge', input.codeChallenge);
    url.searchParams.set('code_challenge_method', 'S256');
    return url.toString();
  }

  async exchangeAuthorizationCode(
    request: AuthorizationCodeExchangeRequest
  ): Promise<AuthSession> {
    const response = await fetch(authRuntimeConfig.tokenEndpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Accept: 'application/json'
      },
      body: new URLSearchParams({
        grant_type: 'authorization_code',
        client_id: authRuntimeConfig.clientId,
        code: request.code,
        redirect_uri: request.redirectUri,
        code_verifier: request.codeVerifier
      }).toString()
    });

    const payload = await this.parseJson(await response.text());

    if (!response.ok) {
      throw new Error(this.readError(payload));
    }

    if (!this.isTokenPayload(payload)) {
      throw new Error('Invalid token response from authentication provider.');
    }

    const expiresIn =
      typeof payload.expires_in === 'number' ? payload.expires_in : 3600;

    return {
      accessToken: payload.access_token,
      expiresAtMs: Date.now() + expiresIn * 1000
    };
  }

  private async parseJson(raw: string): Promise<unknown> {
    if (!raw.trim()) {
      return null;
    }

    try {
      return JSON.parse(raw) as unknown;
    } catch {
      return raw;
    }
  }

  private readError(payload: unknown): string {
    if (payload && typeof payload === 'object') {
      const data = payload as { error?: unknown; error_description?: unknown };
      if (
        typeof data.error_description === 'string' &&
        data.error_description.trim().length > 0
      ) {
        return data.error_description;
      }

      if (typeof data.error === 'string' && data.error.trim().length > 0) {
        return data.error;
      }
    }

    if (typeof payload === 'string' && payload.trim().length > 0) {
      return payload;
    }

    return 'Authentication token request failed.';
  }

  private isTokenPayload(payload: unknown): payload is TokenEndpointResponse {
    return !!(
      payload &&
      typeof payload === 'object' &&
      typeof (payload as { access_token?: unknown }).access_token === 'string' &&
      typeof (payload as { token_type?: unknown }).token_type === 'string'
    );
  }
}
