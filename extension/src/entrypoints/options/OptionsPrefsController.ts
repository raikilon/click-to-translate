import { GetTriggerPrefsUseCase } from "@/content/lookup/application/GetTriggerPrefsUseCase";
import { SaveTriggerPrefsUseCase } from "@/content/lookup/application/SaveTriggerPrefsUseCase";
import type { OptionsView } from "./OptionsView";

export class OptionsPrefsController {
  constructor(
    private readonly view: OptionsView,
    private readonly getTriggerPrefsUseCase: GetTriggerPrefsUseCase,
    private readonly saveTriggerPrefsUseCase: SaveTriggerPrefsUseCase,
  ) {}

  async refreshOptionsPrefs(): Promise<void> {
    const triggerPrefs = await this.getTriggerPrefsUseCase.execute();
    this.view.fillTriggerPrefsForm(triggerPrefs);
  }

  onOptionsFormSubmit(event: Event): void {
    event.preventDefault();
    void this.saveOptionsPrefs().catch(this.onSaveOptionsPrefsFailed.bind(this));
  }

  private async saveOptionsPrefs(): Promise<void> {
    const triggerPrefs = this.view.readTriggerPrefsFromForm();
    await this.saveTriggerPrefsUseCase.execute(triggerPrefs);
    this.view.fillTriggerPrefsForm(triggerPrefs);
    this.view.setStatus("Preferences saved.");
  }

  private onSaveOptionsPrefsFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Save failed.", true);
  }
}





