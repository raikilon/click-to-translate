export interface AuthRuntimeConfig {
  clientId: string;
  authorizationEndpoint: string;
  tokenEndpoint: string;
  redirectUri: string;
  scopes: readonly string[];
}

export const authRuntimeConfig: AuthRuntimeConfig = {
  clientId: 'click-to-translate-web-app',
  authorizationEndpoint:
    'http://localhost:8081/realms/click-to-translate/protocol/openid-connect/auth',
  tokenEndpoint:
    'http://localhost:8081/realms/click-to-translate/protocol/openid-connect/token',
  redirectUri: `${window.location.origin}/auth/callback`,
  scopes: ['openid', 'profile', 'translate', 'vocabulary']
};
