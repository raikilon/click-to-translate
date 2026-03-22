import { GatewayAuthService } from './gateway-auth-service';

export class LogoutUseCase {
  constructor(private readonly gatewayAuthService: GatewayAuthService) {}

  execute(): Promise<void> {
    return this.gatewayAuthService.logout();
  }
}
