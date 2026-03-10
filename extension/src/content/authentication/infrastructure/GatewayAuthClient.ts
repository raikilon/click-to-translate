import { authRuntimeConfig } from "./AuthRuntimeConfig";

export interface CurrentUserResponse {
  name: string;
}

export class GatewayAuthClient {
  async fetchCurrentUser(): Promise<CurrentUserResponse | null> {
    const meUrl = this.buildGatewayUrl(authRuntimeConfig.mePath);
    const response = await fetch(meUrl, {
      method: "GET",
      credentials: "include",
      headers: {
        Accept: "application/json",
      },
    });

    if (response.status === 401) {
      return null;
    }

    if (!response.ok) {
      throw new Error("Current user request failed.");
    }

    const payload = (await response.json()) as CurrentUserResponse;
    if (!payload || typeof payload.name !== "string" || !payload.name.trim()) {
      throw new Error("Current user response is invalid.");
    }

    return payload;
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
