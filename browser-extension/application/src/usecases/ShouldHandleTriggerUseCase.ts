import type { Trigger } from "@domain";
import type { SettingsStore } from "../contracts/SettingsStore";

export class ShouldHandleTriggerUseCase {
  constructor(private readonly settingsStore: SettingsStore) {}

  async execute(trigger: Trigger): Promise<boolean> {
    const settings = await this.settingsStore.get();

    return (
      trigger.mouse.button === settings.mouseButton &&
      trigger.modifiers.alt === settings.modifiers.alt &&
      trigger.modifiers.ctrl === settings.modifiers.ctrl &&
      trigger.modifiers.shift === settings.modifiers.shift &&
      trigger.modifiers.meta === settings.modifiers.meta
    );
  }
}
