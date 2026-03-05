import { AppEnvironment } from './environment.model';

export const environment: AppEnvironment = {
  production: true,
  auth: {
    clientId: 'click-to-translate-web-app',
    keycloakBaseUrl: 'http://localhost:8081',
    realm: 'click-to-translate',
    scopes: ['openid', 'translate', 'vocabulary']
  }
};
