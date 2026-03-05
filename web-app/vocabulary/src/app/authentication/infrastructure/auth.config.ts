import { environment } from '../../../environments/environment';

export interface AuthRuntimeConfig {
  clientId: string;
  authorizationEndpoint: string;
  tokenEndpoint: string;
  redirectUri: string;
  scopes: readonly string[];
}

const authIssuer = `${environment.auth.keycloakBaseUrl}/realms/${environment.auth.realm}`;

export const authRuntimeConfig: AuthRuntimeConfig = {
  clientId: environment.auth.clientId,
  authorizationEndpoint: `${authIssuer}/protocol/openid-connect/auth`,
  tokenEndpoint: `${authIssuer}/protocol/openid-connect/token`,
  redirectUri: `${window.location.origin}/auth/callback`,
  scopes: environment.auth.scopes
};
