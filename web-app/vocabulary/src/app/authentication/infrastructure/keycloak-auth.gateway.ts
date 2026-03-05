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
  refresh_token?: string;
  refresh_expires_in?: number;
  id_token?: string;
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
    const payload = await this.tokenRequest({
      grant_type: 'authorization_code',
      client_id: authRuntimeConfig.clientId,
      code: request.code,
      redirect_uri: request.redirectUri,
      code_verifier: request.codeVerifier
    });

    return this.asAuthSession(payload, Date.now());
  }

  async refreshAccessToken(refreshToken: string): Promise<AuthSession> {
    const payload = await this.tokenRequest({
      grant_type: 'refresh_token',
      client_id: authRuntimeConfig.clientId,
      refresh_token: refreshToken
    });

    return this.asAuthSession(payload, Date.now());
  }

  private async tokenRequest(input: Record<string, string>): Promise<TokenEndpointResponse> {
    const response = await fetch(authRuntimeConfig.tokenEndpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        Accept: 'application/json'
      },
      body: new URLSearchParams(input).toString()
    });

    const payload = await this.parseJson(await response.text());

    if (!response.ok) {
      throw new Error(this.readError(payload));
    }

    if (!this.isTokenPayload(payload)) {
      throw new Error('Invalid token response from authentication provider.');
    }

    return payload;
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

  private asAuthSession(payload: TokenEndpointResponse, nowMs: number): AuthSession {
    const accessTokenExpiresIn =
      typeof payload.expires_in === 'number' ? payload.expires_in : 3600;
    const refreshTokenExpiresIn =
      typeof payload.refresh_expires_in === 'number'
        ? payload.refresh_expires_in
        : accessTokenExpiresIn;

    return {
      accessToken: payload.access_token,
      accessTokenExpiresAtMs: nowMs + accessTokenExpiresIn * 1000,
      refreshToken: payload.refresh_token ?? '',
      refreshTokenExpiresAtMs: nowMs + refreshTokenExpiresIn * 1000,
      idToken: payload.id_token
    };
  }
}
