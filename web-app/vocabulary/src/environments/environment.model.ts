export interface AppEnvironment {
  production: boolean;
  auth: {
    clientId: string;
    keycloakBaseUrl: string;
    realm: string;
    scopes: readonly string[];
  };
}
