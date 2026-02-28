import { OptionsPrefsController } from "./OptionsPrefsController";
import { OptionsView } from "./OptionsView";

export class OptionsPage {
  private readonly view: OptionsView;
  private readonly prefsController: OptionsPrefsController;

  constructor() {
    this.view = new OptionsView();
    this.prefsController = new OptionsPrefsController(this.view);
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
