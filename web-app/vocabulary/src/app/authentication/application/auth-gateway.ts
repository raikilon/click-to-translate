import { AuthSession } from '../domain/auth-session';

export interface AuthorizationCodeExchangeRequest {
  code: string;
  codeVerifier: string;
  redirectUri: string;
}

export interface AuthGateway {
  createAuthorizationUrl(input: {
    state: string;
    codeChallenge: string;
    redirectUri: string;
  }): string;

  exchangeAuthorizationCode(
    request: AuthorizationCodeExchangeRequest
  ): Promise<AuthSession>;
}
