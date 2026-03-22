import type { CurrentUser } from './current-user';
import { GatewayAuthService } from './gateway-auth-service';

export class GetCurrentUserUseCase {
  constructor(private readonly gatewayAuthService: GatewayAuthService) {}

  execute(): Promise<CurrentUser | null> {
    return this.gatewayAuthService.getCurrentUser();
  }
}
