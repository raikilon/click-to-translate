interface AuthRuntimeConfig {
  apiBaseUrl: string;
  loginPath: string;
  mePath: string;
  logoutPath: string;
}

export class AuthRuntimeConfigFactory {
  static create(): AuthRuntimeConfig {
    return {
      apiBaseUrl: this.readRequiredEnvString("WXT_API_BASE_URL"),
      loginPath: this.readRequiredEnvString("WXT_AUTH_LOGIN_PATH"),
      mePath: this.readRequiredEnvString("WXT_AUTH_ME_PATH"),
      logoutPath: this.readRequiredEnvString("WXT_AUTH_LOGOUT_PATH"),
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

}

export const authRuntimeConfig: AuthRuntimeConfig = AuthRuntimeConfigFactory.create();
