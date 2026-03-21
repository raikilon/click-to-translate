import { authRuntimeConfig } from "./AuthRuntimeConfig";

export class GatewayAuthClient {
  async isAuthenticated(): Promise<boolean> {
    const meUrl = this.buildGatewayUrl(authRuntimeConfig.mePath);
    const response = await fetch(meUrl, {
      method: "GET",
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    if (response.status === 401) {
      return false;
    }

    if (!response.ok) {
      throw new Error("Current user request failed.");
    }

    return true;
  }

  async logout(): Promise<void> {
    const logoutUrl = this.buildGatewayUrl(authRuntimeConfig.logoutPath);
    const response = await fetch(logoutUrl, {
      method: "POST",
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    if (response.ok || response.status === 401) {
      return;
    }

    throw new Error("Logout request failed.");
  }

  getLoginUrl(): string {
    return this.buildGatewayUrl(authRuntimeConfig.loginPath);
  }

  getGatewayOrigin(): string {
    const apiBaseUrl = this.tryParseUrl(authRuntimeConfig.apiBaseUrl);
    if (!apiBaseUrl) {
      throw new Error("WXT_API_BASE_URL is not a valid URL.");
    }

    return apiBaseUrl.origin;
  }

  private buildGatewayUrl(pathOrAbsolute: string): string {
    return new URL(pathOrAbsolute, authRuntimeConfig.apiBaseUrl).toString();
  }

  private tryParseUrl(url: string): URL | null {
    try {
      return new URL(url);
    } catch {
      return null;
    }
  }
}
