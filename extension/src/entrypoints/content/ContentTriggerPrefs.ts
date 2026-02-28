import {
  DEFAULT_TRIGGER_PREFS,
  type TriggerMouseButton,
  type TriggerPrefs,
} from "@domain";
import { triggerPrefsStorageItem } from "../background/storage/items";

export class ContentTriggerPrefs {
  private current: TriggerPrefs = {
    mouseButton: DEFAULT_TRIGGER_PREFS.mouseButton,
    modifiers: { ...DEFAULT_TRIGGER_PREFS.modifiers },
  };

  initialize(): void {
    void this.refreshFromStorage().catch(this.ignoreTriggerPrefsRefreshFailure);

    triggerPrefsStorageItem.watch(this.onStorageChanged.bind(this));
  }

  matchesEvent(event: MouseEvent): boolean {
    return (
      this.toTriggerMouseButton(event.button) === this.current.mouseButton &&
      event.altKey === this.current.modifiers.alt &&
      event.ctrlKey === this.current.modifiers.ctrl &&
      event.shiftKey === this.current.modifiers.shift &&
      event.metaKey === this.current.modifiers.meta
    );
  }

  private async refreshFromStorage(): Promise<void> {
    this.current = await triggerPrefsStorageItem.getValue();
  }

  private onStorageChanged(prefs: TriggerPrefs): void {
    this.current = prefs;
  }

  private toTriggerMouseButton(button: number): TriggerMouseButton {
    if (button === 1) {
      return "middle";
    }

    if (button === 2) {
      return "right";
    }

    return "left";
  }

  private ignoreTriggerPrefsRefreshFailure(_error: unknown): void {}
}
