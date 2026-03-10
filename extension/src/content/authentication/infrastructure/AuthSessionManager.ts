import type { AuthState } from "@/content/authentication/domain/AuthState";
import type { AuthStateProvider } from "@/content/authentication/application/AuthStateProvider";
import type { IAuthenticationService } from "@/content/authentication/application/IAuthenticationService";
import { GatewayAuthClient, type CurrentUserResponse } from "./GatewayAuthClient";
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
    const currentUser = await this.dependencies.gatewayAuthClient.fetchCurrentUser();
    if (!currentUser) {
      return { isLoggedIn: false };
    }

    return this.toAuthState(currentUser);
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

  private toAuthState(_currentUser: CurrentUserResponse): AuthState {
    return {
      isLoggedIn: true,
    };
  }

}
