import type { LanguagePrefs } from "@domain";

export type PopupStatusKind = "error" | "ok";

export class PopupView {
  bindLoginButtonClick(listener: () => void): void {
    this.byId<HTMLButtonElement>("loginButton").addEventListener("click", listener);
  }

  bindLogoutButtonClick(listener: () => void): void {
    this.byId<HTMLButtonElement>("logoutButton").addEventListener("click", listener);
  }

  bindSavePrefsButtonClick(listener: () => void): void {
    this.byId<HTMLButtonElement>("savePrefsButton").addEventListener("click", listener);
  }

  setStatus(message: string, isError = false): void {
    const status = this.byId<HTMLDivElement>("status");
    const kind: PopupStatusKind = isError ? "error" : "ok";
    status.textContent = message;
    status.dataset.kind = kind;
  }

  readPrefsFromForm(): LanguagePrefs {
    const sourceLanguageId = this.byId<HTMLInputElement>("sourceLanguageId").value;
    const targetLanguageId = this.byId<HTMLInputElement>("targetLanguageId").value;

    return {
      sourceLanguageId: sourceLanguageId || undefined,
      targetLanguageId: targetLanguageId || undefined,
    };
  }

  fillPrefsForm(prefs: LanguagePrefs): void {
    const sourceLanguageInput = this.byId<HTMLInputElement>("sourceLanguageId");
    const targetLanguageInput = this.byId<HTMLInputElement>("targetLanguageId");

    if (prefs.sourceLanguageId !== undefined) {
      sourceLanguageInput.value = prefs.sourceLanguageId;
    }

    if (prefs.targetLanguageId !== undefined) {
      targetLanguageInput.value = prefs.targetLanguageId;
    }
  }

  renderSessionStateAndToggleAuthButtons(loggedIn: boolean): void {
    const sessionLabel = this.byId<HTMLDivElement>("sessionState");
    const loginButton = this.byId<HTMLButtonElement>("loginButton");
    const logoutButton = this.byId<HTMLButtonElement>("logoutButton");

    sessionLabel.textContent = loggedIn ? "Session: Logged in" : "Session: Logged out";
    loginButton.hidden = loggedIn;
    logoutButton.hidden = !loggedIn;
  }

  private byId<T extends HTMLElement>(id: string): T {
    const element = document.getElementById(id);
    if (!element) {
      throw new Error(`Missing popup element: ${id}`);
    }

    return element as T;
  }
}
