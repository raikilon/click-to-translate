import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";

export class ModifierKeyState {
  private alt = false;
  private ctrl = false;
  private shift = false;
  private meta = false;

  onKeyDown(event: KeyboardEvent): void {
    this.sync(event);
    this.applyKey(event.key, true);
  }

  onKeyUp(event: KeyboardEvent): void {
    this.sync(event);
    this.applyKey(event.key, false);
  }

  reset(): void {
    this.alt = false;
    this.ctrl = false;
    this.shift = false;
    this.meta = false;
  }

  matches(prefs: TriggerPrefs): boolean {
    return (
      this.alt === prefs.modifiers.alt &&
      this.ctrl === prefs.modifiers.ctrl &&
      this.shift === prefs.modifiers.shift &&
      this.meta === prefs.modifiers.meta
    );
  }

  private sync(event: KeyboardEvent): void {
    this.alt = event.altKey;
    this.ctrl = event.ctrlKey;
    this.shift = event.shiftKey;
    this.meta = event.metaKey;
  }

  private applyKey(key: string, pressed: boolean): void {
    const normalized = key.toLowerCase();
    if (normalized === "alt") {
      this.alt = pressed;
    }

    if (normalized === "control") {
      this.ctrl = pressed;
    }

    if (normalized === "shift") {
      this.shift = pressed;
    }

    if (normalized === "meta") {
      this.meta = pressed;
    }
  }
}





