import { GetTriggerPrefsUseCase } from "@/content/lookup/application/GetTriggerPrefsUseCase";
import { SaveTriggerPrefsUseCase } from "@/content/lookup/application/SaveTriggerPrefsUseCase";
import { GetHighlightStyleUseCase } from "@/content/popup/application/GetHighlightStyleUseCase";
import { SaveHighlightStyleUseCase } from "@/content/popup/application/SaveHighlightStyleUseCase";
import type { OptionsView } from "./OptionsView";

export class OptionsPrefsController {
  constructor(
    private readonly view: OptionsView,
    private readonly getTriggerPrefsUseCase: GetTriggerPrefsUseCase,
    private readonly saveTriggerPrefsUseCase: SaveTriggerPrefsUseCase,
    private readonly getHighlightStyleUseCase: GetHighlightStyleUseCase,
    private readonly saveHighlightStyleUseCase: SaveHighlightStyleUseCase,
  ) {}

  async refreshOptionsPrefs(): Promise<void> {
    const triggerPrefs = await this.getTriggerPrefsUseCase.execute();
    const highlightStyleId = await this.getHighlightStyleUseCase.execute();
    this.view.fillTriggerPrefsForm(triggerPrefs);
    this.view.fillHighlightStyleIdForm(highlightStyleId);
  }

  onOptionsFormSubmit(event: Event): void {
    event.preventDefault();
    void this.saveOptionsPrefs().catch(this.onSaveOptionsPrefsFailed.bind(this));
  }

  private async saveOptionsPrefs(): Promise<void> {
    const triggerPrefs = this.view.readTriggerPrefsFromForm();
    const highlightStyleId = this.view.readHighlightStyleIdFromForm();
    await this.saveTriggerPrefsUseCase.execute(triggerPrefs);
    await this.saveHighlightStyleUseCase.execute(highlightStyleId);
    this.view.fillTriggerPrefsForm(triggerPrefs);
    this.view.fillHighlightStyleIdForm(highlightStyleId);
    this.view.setStatus("Preferences saved.");
  }

  private onSaveOptionsPrefsFailed(error: unknown): void {
    this.view.setStatus(error instanceof Error ? error.message : "Save failed.", true);
  }
}





