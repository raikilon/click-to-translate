import type { AuthState } from './auth-state';
import { GatewayAuthService } from './gateway-auth-service';

export class GetAuthStateUseCase {
  constructor(private readonly gatewayAuthService: GatewayAuthService) {}

  execute(): Promise<AuthState> {
    return this.gatewayAuthService.getAuthState();
  }
}
