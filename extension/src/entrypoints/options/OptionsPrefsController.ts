import {
  languagePrefsStorageItem,
  triggerPrefsStorageItem,
} from "../background/storage/items";
import type { OptionsView } from "./OptionsView";

export class OptionsPrefsController {
  constructor(private readonly view: OptionsView) {}

  async refreshOptionsPrefs(): Promise<void> {
    const [languagePrefs, triggerPrefs] = await Promise.all([
      languagePrefsStorageItem.getValue(),
      triggerPrefsStorageItem.getValue(),
    ]);
    this.view.fillLanguagePrefsForm(languagePrefs);
    this.view.fillTriggerPrefsForm(triggerPrefs);
  }

  onOptionsFormSubmit(event: Event): void {
    event.preventDefault();
    void this.saveOptionsPrefs().catch(this.onSaveOptionsPrefsFailed.bind(this));
  }

  private async saveOptionsPrefs(): Promise<void> {
    const languagePrefs = this.view.readLanguagePrefsFromForm();
    const triggerPrefs = this.view.readTriggerPrefsFromForm();
    await Promise.all([
      languagePrefsStorageItem.setValue(languagePrefs),
      triggerPrefsStorageItem.setValue(triggerPrefs),
    ]);
    this.view.fillLanguagePrefsForm(languagePrefs);
    this.view.fillTriggerPrefsForm(triggerPrefs);
    this.view.setStatus("Preferences saved.");
  }

  private onSaveOptionsPrefsFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Save failed.", true);
  }
}
