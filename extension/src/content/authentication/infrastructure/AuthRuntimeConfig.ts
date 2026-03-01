interface AuthRuntimeConfig {
  authAuthorizeUrl: string;
  authTokenUrl: string;
  oauthClientId: string;
  scopes: string[];
}

export class AuthRuntimeConfigFactory {
  static create(): AuthRuntimeConfig {
    return {
      authAuthorizeUrl: this.readRequiredEnvString("WXT_AUTH_AUTHORIZE_URL"),
      authTokenUrl: this.readRequiredEnvString("WXT_AUTH_TOKEN_URL"),
      oauthClientId: this.readRequiredEnvString("WXT_OAUTH_CLIENT_ID"),
      scopes: this.readRequiredEnvList("WXT_AUTH_SCOPES"),
    };
  }

  private static readEnvString(name: string): string | undefined {
    const value = (import.meta.env[name] as string | undefined)?.trim();
    return value || undefined;
  }

  private static readRequiredEnvString(name: string): string {
    const value = this.readEnvString(name);
    if (!value) {
      throw new Error(`Missing required runtime config env var: ${name}`);
    }

    return value;
  }

  private static readRequiredEnvList(name: string): string[] {
    const value = this.readRequiredEnvString(name);
    const parsed = this.parseList(value);
    if (parsed.length === 0) {
      throw new Error(`Missing required runtime config list env var: ${name}`);
    }

    return parsed;
  }

  private static parseList(value: string): string[] {
    return value
      .split(/[\s,]+/)
      .map((item) => item.trim())
      .filter(Boolean);
  }
}

export const authRuntimeConfig: AuthRuntimeConfig = AuthRuntimeConfigFactory.create();
