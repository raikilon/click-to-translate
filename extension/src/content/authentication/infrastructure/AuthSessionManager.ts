import type { AuthState } from "@/content/authentication/domain/AuthState";
import type { AuthStateProvider } from "@/content/authentication/application/AuthStateProvider";
import type { IAuthenticationService } from "@/content/authentication/application/IAuthenticationService";
import { GatewayAuthClient } from "./GatewayAuthClient";
import { LoginTabAuthFlow } from "./LoginTabAuthFlow";

interface AuthSessionManagerDependencies {
  gatewayAuthClient: GatewayAuthClient;
  loginTabAuthFlow: LoginTabAuthFlow;
}

export class AuthSessionManager
  implements AuthStateProvider, IAuthenticationService
{
  constructor(private readonly dependencies: AuthSessionManagerDependencies) {}

  async getAuthState(): Promise<AuthState> {
    const isAuthenticated = await this.dependencies.gatewayAuthClient.isAuthenticated();
    if (!isAuthenticated) {
      return { isLoggedIn: false };
    }

    return { isLoggedIn: true };
  }

  async login(): Promise<AuthState> {
    await this.dependencies.loginTabAuthFlow.start(
      this.dependencies.gatewayAuthClient.getLoginUrl(),
      this.dependencies.gatewayAuthClient.getGatewayOrigin(),
    );
    return { isLoggedIn: false };
  }

  async logout(): Promise<void> {
    await this.dependencies.gatewayAuthClient.logout();
  }

}
