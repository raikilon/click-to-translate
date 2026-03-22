import type { TriggerMouseButton, TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";

export class ClickGate {
  matches(event: MouseEvent, prefs: TriggerPrefs): boolean {
    return this.toTriggerMouseButton(event.button) === prefs.mouseButton;
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
}





