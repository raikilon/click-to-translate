import type { BackgroundClient } from "../shared/messaging/client";
import type { PopupView } from "./PopupView";

export class PopupAuthController {
  constructor(
    private readonly client: BackgroundClient,
    private readonly view: PopupView,
  ) {}

  async refreshAuthState(): Promise<void> {
    const authState = await this.client.getAuthState();
    this.view.renderSessionStateAndToggleAuthButtons(authState.isLoggedIn);
  }

  onLoginButtonClick(): void {
    void this.loginAndRender().catch(this.onLoginFailed.bind(this));
  }

  onLogoutButtonClick(): void {
    void this.logoutAndRender().catch(this.onLogoutFailed.bind(this));
  }

  private async loginAndRender(): Promise<void> {
    const state = await this.client.login();
    this.view.renderSessionStateAndToggleAuthButtons(state.isLoggedIn);
    this.view.setStatus(
      state.isLoggedIn ? "Login successful." : "Login failed.",
      !state.isLoggedIn,
    );
  }

  private async logoutAndRender(): Promise<void> {
    await this.client.logout();
    this.view.renderSessionStateAndToggleAuthButtons(false);
    this.view.setStatus("Logged out.");
  }

  private onLoginFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Login failed.", true);
  }

  private onLogoutFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Logout failed.", true);
  }
}
