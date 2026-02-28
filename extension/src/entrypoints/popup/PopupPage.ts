import type { BackgroundClient } from "../shared/messaging/client";
import { PopupAuthController } from "./PopupAuthController";
import { PopupPrefsController } from "./PopupPrefsController";
import { PopupView } from "./PopupView";

interface PopupDependencies {
  client: BackgroundClient;
}

export class PopupPage {
  private readonly view: PopupView;
  private readonly authController: PopupAuthController;
  private readonly prefsController: PopupPrefsController;

  constructor(dependencies: PopupDependencies) {
    this.view = new PopupView();
    this.authController = new PopupAuthController(dependencies.client, this.view);
    this.prefsController = new PopupPrefsController(this.view);
  }

  register(): void {
    this.view.bindLoginButtonClick(
      this.authController.onLoginButtonClick.bind(this.authController),
    );
    this.view.bindLogoutButtonClick(
      this.authController.onLogoutButtonClick.bind(this.authController),
    );
    this.view.bindSavePrefsButtonClick(
      this.prefsController.onSavePrefsButtonClick.bind(this.prefsController),
    );

    void this.refreshState().catch(this.onPopupStateRefreshFailed.bind(this));
  }

  private async refreshState(): Promise<void> {
    await Promise.all([
      this.authController.refreshAuthState(),
      this.prefsController.refreshLanguagePrefs(),
    ]);
  }

  private onPopupStateRefreshFailed(error: unknown): void {
    this.view.setStatus(
      error instanceof Error ? error.message : "Failed to load popup state.",
      true,
    );
  }
}
