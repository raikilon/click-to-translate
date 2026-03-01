import { OptionsPrefsController } from "./OptionsPrefsController";
import { OptionsView } from "./OptionsView";
import { GetTriggerPrefsUseCase } from "@/content/lookup/application/GetTriggerPrefsUseCase";
import { SaveTriggerPrefsUseCase } from "@/content/lookup/application/SaveTriggerPrefsUseCase";
import { LookupTriggerPrefsRepository } from "@/content/lookup/infrastructure/LookupTriggerPrefsRepository";

export class OptionsPage {
  private readonly view: OptionsView;
  private readonly prefsController: OptionsPrefsController;

  constructor() {
    this.view = new OptionsView();
    const triggerPrefsRepository = new LookupTriggerPrefsRepository();
    this.prefsController = new OptionsPrefsController(
      this.view,
      new GetTriggerPrefsUseCase(triggerPrefsRepository),
      new SaveTriggerPrefsUseCase(triggerPrefsRepository),
    );
  }

  register(): void {
    this.view.bindOptionsFormSubmit(
      this.prefsController.onOptionsFormSubmit.bind(this.prefsController),
    );

    void this.refreshOptionsState().catch(this.onOptionsStateRefreshFailed.bind(this));
  }

  private async refreshOptionsState(): Promise<void> {
    await this.prefsController.refreshOptionsPrefs();
  }

  private onOptionsStateRefreshFailed(error: unknown): void {
    this.view.setStatus(
      error instanceof Error ? error.message : "Failed to load options.",
      true,
    );
  }
}





