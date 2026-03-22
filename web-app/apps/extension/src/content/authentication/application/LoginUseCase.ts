import {
  type AuthState,
  GatewayAuthService,
} from '@vocabulary/auth';
import { LoginTabAuthFlow } from "@/content/authentication/infrastructure/LoginTabAuthFlow";

export class LoginUseCase {
  constructor(
    private readonly gatewayAuthService: GatewayAuthService,
    private readonly loginTabAuthFlow: LoginTabAuthFlow,
  ) {}

  async execute(): Promise<AuthState> {
    await this.loginTabAuthFlow.start(
      this.gatewayAuthService.getLoginUrl(),
      this.gatewayAuthService.getGatewayOrigin(),
    );

    return { isAuthenticated: false };
  }
}
