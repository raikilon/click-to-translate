import type { BackgroundClient } from "../shared/messaging/client";
import type { AuthState } from "@application";

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
