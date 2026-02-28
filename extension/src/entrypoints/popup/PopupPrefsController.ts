import { languagePrefsStorageItem } from "../background/storage/items";
import type { PopupView } from "./PopupView";

export class PopupPrefsController {
  constructor(private readonly view: PopupView) {}

  async refreshLanguagePrefs(): Promise<void> {
    const prefs = await languagePrefsStorageItem.getValue();
    this.view.fillPrefsForm(prefs);
  }

  onSavePrefsButtonClick(): void {
    void this.savePrefs().catch(this.onSavePrefsFailed.bind(this));
  }

  private async savePrefs(): Promise<void> {
    const prefs = this.view.readPrefsFromForm();
    await languagePrefsStorageItem.setValue(prefs);
    this.view.fillPrefsForm(prefs);
    this.view.setStatus("Language preferences saved.");
  }

  private onSavePrefsFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Save failed.", true);
  }
}
