import {
  triggerPrefsStorageItem,
} from "../background/storage/items";
import type { OptionsView } from "./OptionsView";

export class OptionsPrefsController {
  constructor(private readonly view: OptionsView) {}

  async refreshOptionsPrefs(): Promise<void> {
    const triggerPrefs = await triggerPrefsStorageItem.getValue();
    this.view.fillTriggerPrefsForm(triggerPrefs);
  }

  onOptionsFormSubmit(event: Event): void {
    event.preventDefault();
    void this.saveOptionsPrefs().catch(this.onSaveOptionsPrefsFailed.bind(this));
  }

  private async saveOptionsPrefs(): Promise<void> {
    const triggerPrefs = this.view.readTriggerPrefsFromForm();
    await triggerPrefsStorageItem.setValue(triggerPrefs);
    this.view.fillTriggerPrefsForm(triggerPrefs);
    this.view.setStatus("Preferences saved.");
  }

  private onSaveOptionsPrefsFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Save failed.", true);
  }
}
