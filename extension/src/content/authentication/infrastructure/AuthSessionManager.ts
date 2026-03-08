import type { AuthState } from "@/content/authentication/domain/AuthState";
import type { AuthStateProvider } from "@/content/authentication/application/AuthStateProvider";
import type { IAuthenticationService } from "@/content/authentication/application/IAuthenticationService";
import { authRuntimeConfig } from "./AuthRuntimeConfig";

interface CurrentUserResponse {
  name: string;
}

export class AuthSessionManager
  implements AuthStateProvider, IAuthenticationService
{
  async getAuthState(): Promise<AuthState> {
    const currentUser = await this.fetchCurrentUser();
    if (!currentUser) {
      return { isLoggedIn: false };
    }

    return this.toAuthState(currentUser);
  }

  async login(): Promise<AuthState> {
    await this.openLoginPage();
    return this.getAuthState();
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

  private async fetchCurrentUser(): Promise<CurrentUserResponse | null> {
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

  private toAuthState(_currentUser: CurrentUserResponse): AuthState {
    return {
      isLoggedIn: true,
    };
  }

  private async openLoginPage(): Promise<void> {
    if (!browser.tabs?.create) {
      throw new Error("browser.tabs.create is not available in this build.");
    }

    await browser.tabs.create({
      url: this.buildGatewayUrl(authRuntimeConfig.loginPath),
      active: true,
    });
  }

  private buildGatewayUrl(pathOrAbsolute: string): string {
    return new URL(pathOrAbsolute, authRuntimeConfig.apiBaseUrl).toString();
  }
}
