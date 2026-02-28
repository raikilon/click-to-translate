import type { BackgroundClient } from "../shared/messaging/client";
import type { LanguagePrefs } from "@domain";
import { PopupAuthController } from "./PopupAuthController";
import { PopupLanguageController } from "./PopupLanguageController";
import { PopupPrefsController } from "./PopupPrefsController";
import { PopupView } from "./PopupView";

interface PopupDependencies {
  client: BackgroundClient;
}

export class PopupPage {
  private readonly view: PopupView;
  private readonly authController: PopupAuthController;
  private readonly languageController: PopupLanguageController;
  private currentLanguagePrefs: LanguagePrefs = {
    sourceLanguageId: undefined,
    targetLanguageId: undefined,
  };

  constructor(dependencies: PopupDependencies) {
    this.view = new PopupView();
    const prefsController = new PopupPrefsController();
    this.authController = new PopupAuthController(dependencies.client);
    this.languageController = new PopupLanguageController(
      dependencies.client,
      prefsController,
    );
  }

  register(): void {
    this.view.bindLoginButtonClick(
      this.onLoginButtonClick.bind(this),
    );
    this.view.bindLogoutButtonClick(
      this.onLogoutButtonClick.bind(this),
    );
    this.view.bindLanguageSelectionChanged(this.onLanguageSelectionChanged.bind(this));

    void this.refreshInitialState().catch(this.onPopupStateRefreshFailed.bind(this));
  }

  private async refreshInitialState(): Promise<void> {
    const authState = await this.authController.getAuthState();
    if (!authState.isLoggedIn) {
      this.renderLoggedOutUi();
      return;
    }

    await this.showAuthenticatedUi();
  }

  private async showAuthenticatedUi(): Promise<void> {
    this.view.renderSessionStateAndToggleAuthButtons(true);
    const languageIds = await this.languageController.listAvailableLanguageIds();
    this.view.populateLanguageOptions(languageIds);
    this.currentLanguagePrefs = await this.languageController.loadLanguagePrefs();
    this.view.fillPrefsForm(this.currentLanguagePrefs);
  }

  private renderLoggedOutUi(): void {
    this.view.renderSessionStateAndToggleAuthButtons(false);
    this.view.renderLoggedOutState();
  }

  private onLanguageSelectionChanged(): void {
    void this.persistLanguageSelection().catch(this.onPopupStateRefreshFailed.bind(this));
  }

  private async persistLanguageSelection(): Promise<void> {
    this.currentLanguagePrefs = this.view.readPrefsFromForm();
    await this.languageController.saveLanguagePrefs(this.currentLanguagePrefs);
    this.view.setStatus("Language preference saved.");
  }

  private onLoginButtonClick(): void {
    void this.loginAndRefresh().catch(this.onPopupStateRefreshFailed.bind(this));
  }

  private onLogoutButtonClick(): void {
    void this.logoutAndRefresh().catch(this.onPopupStateRefreshFailed.bind(this));
  }

  private async loginAndRefresh(): Promise<void> {
    const state = await this.authController.login();
    if (!state.isLoggedIn) {
      this.renderLoggedOutUi();
      this.view.setStatus("Login failed.", true);
      return;
    }

    await this.showAuthenticatedUi();
    this.view.setStatus("Login successful.");
  }

  private async logoutAndRefresh(): Promise<void> {
    await this.authController.logout();
    this.renderLoggedOutUi();
    this.view.setStatus("Logged out.");
  }

  private onPopupStateRefreshFailed(error: unknown): void {
    this.view.setStatus(
      error instanceof Error ? error.message : "Failed to load popup state.",
      true,
    );
  }
}
