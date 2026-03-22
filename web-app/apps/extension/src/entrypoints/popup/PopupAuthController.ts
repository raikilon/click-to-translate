import type { BackgroundClient } from "../shared/messaging/client";
import type { AuthState } from '@vocabulary/auth';

export class PopupAuthController {
  constructor(private readonly client: BackgroundClient) {}

  getAuthState(): Promise<AuthState> {
    return this.client.getAuthState();
  }

  login(): Promise<AuthState> {
    return this.client.login();
  }

  async logout(): Promise<void> {
    await this.client.logout();
  }
}





