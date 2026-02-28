import type { LanguagePrefs } from "@domain";

export type PopupStatusKind = "error" | "ok";

export class PopupView {
  constructor() {
    this.populateLanguageOptions([]);
  }

  bindLoginButtonClick(listener: () => void): void {
    this.byId<HTMLButtonElement>("loginButton").addEventListener("click", listener);
  }

  bindLogoutButtonClick(listener: () => void): void {
    this.byId<HTMLButtonElement>("logoutButton").addEventListener("click", listener);
  }

  bindLanguageSelectionChanged(listener: () => void): void {
    this.byId<HTMLSelectElement>("sourceLanguageId").addEventListener("change", listener);
    this.byId<HTMLSelectElement>("targetLanguageId").addEventListener("change", listener);
  }

  setStatus(message: string, isError = false): void {
    const status = this.byId<HTMLDivElement>("status");
    const kind: PopupStatusKind = isError ? "error" : "ok";
    status.textContent = message;
    status.dataset.kind = kind;
  }

  readPrefsFromForm(): LanguagePrefs {
    const sourceLanguageId = this.byId<HTMLSelectElement>("sourceLanguageId").value;
    const targetLanguageId = this.byId<HTMLSelectElement>("targetLanguageId").value;

    return {
      sourceLanguageId: sourceLanguageId || undefined,
      targetLanguageId: targetLanguageId || undefined,
    };
  }

  fillPrefsForm(prefs: LanguagePrefs): void {
    const sourceLanguageInput = this.byId<HTMLSelectElement>("sourceLanguageId");
    const targetLanguageInput = this.byId<HTMLSelectElement>("targetLanguageId");

    sourceLanguageInput.value = prefs.sourceLanguageId ?? "";
    targetLanguageInput.value = prefs.targetLanguageId ?? "";
  }

  setLanguageSectionVisible(visible: boolean): void {
    this.byId<HTMLElement>("languageSection").hidden = !visible;
  }

  populateLanguageOptions(languageIds: string[]): void {
    const sourceSelect = this.byId<HTMLSelectElement>("sourceLanguageId");
    const targetSelect = this.byId<HTMLSelectElement>("targetLanguageId");
    this.replaceOptions(sourceSelect, languageIds);
    this.replaceOptions(targetSelect, languageIds);
  }

  renderLoggedOutState(): void {
    this.setLanguageSectionVisible(false);
    this.populateLanguageOptions([]);
    this.fillPrefsForm({
      sourceLanguageId: undefined,
      targetLanguageId: undefined,
    });
  }

  renderSessionStateAndToggleAuthButtons(loggedIn: boolean): void {
    const sessionLabel = this.byId<HTMLDivElement>("sessionState");
    const loginButton = this.byId<HTMLButtonElement>("loginButton");
    const logoutButton = this.byId<HTMLButtonElement>("logoutButton");
    const optionsLink = this.byId<HTMLAnchorElement>("optionsLink");

    sessionLabel.textContent = loggedIn ? "Session: Logged in" : "Session: Logged out";
    loginButton.hidden = loggedIn;
    logoutButton.hidden = !loggedIn;
    optionsLink.hidden = !loggedIn;
    this.setLanguageSectionVisible(loggedIn);
  }

  private replaceOptions(select: HTMLSelectElement, languageIds: string[]): void {
    const previous = select.value;
    select.innerHTML = "";
    select.appendChild(new Option("Select a language", ""));
    for (const languageId of languageIds) {
      select.appendChild(new Option(languageId, languageId));
    }
    select.value = languageIds.includes(previous) ? previous : "";
  }

  private byId<T extends HTMLElement>(id: string): T {
    const element = document.getElementById(id);
    if (!element) {
      throw new Error(`Missing popup element: ${id}`);
    }

    return element as T;
  }
}
