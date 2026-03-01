import { DEFAULT_TRIGGER_PREFS, type TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";
import type { IPrefsRepository } from "@/content/lookup/application/IPrefsRepository";
import {
  triggerPrefsStorageItem,
  hoverDelayMsStorageItem,
} from "@/content/lookup/infrastructure/LookupPrefsStorage";

interface StorageChangeEntry {
  newValue?: unknown;
}

interface StorageChanges {
  [key: string]: StorageChangeEntry;
}

export class BrowserPrefsRepository implements IPrefsRepository {
  private triggerPrefs: TriggerPrefs = {
    mouseButton: DEFAULT_TRIGGER_PREFS.mouseButton,
    modifiers: { ...DEFAULT_TRIGGER_PREFS.modifiers },
  };

  private hoverDelayMs = 500;
  private readonly initialization: Promise<void>;

  constructor() {
    this.initialization = this.loadInitialSnapshot();
    this.bindStorageChanges();
  }

  async getTriggerPrefs(): Promise<TriggerPrefs> {
    await this.initialization;
    return {
      mouseButton: this.triggerPrefs.mouseButton,
      modifiers: { ...this.triggerPrefs.modifiers },
    };
  }

  async getHoverDelayMs(): Promise<number> {
    await this.initialization;
    return this.hoverDelayMs;
  }

  private async loadInitialSnapshot(): Promise<void> {
    this.triggerPrefs = await triggerPrefsStorageItem.getValue();
    this.hoverDelayMs = await hoverDelayMsStorageItem.getValue();
  }

  private bindStorageChanges(): void {
    const api = browser.storage?.onChanged;
    if (!api) {
      return;
    }

    api.addListener(this.onStorageChanged.bind(this));
  }

  private onStorageChanged(changes: StorageChanges, areaName: string): void {
    if (areaName !== "local") {
      return;
    }

    const triggerChange = changes.triggerPrefs ?? changes["local:triggerPrefs"];
    if (triggerChange?.newValue && this.isTriggerPrefs(triggerChange.newValue)) {
      this.triggerPrefs = triggerChange.newValue;
    }

    const hoverChange = changes.hoverDelayMs ?? changes["local:hoverDelayMs"];
    if (typeof hoverChange?.newValue === "number") {
      this.hoverDelayMs = Math.max(50, hoverChange.newValue);
    }
  }

  private isTriggerPrefs(value: unknown): value is TriggerPrefs {
    if (!value || typeof value !== "object") {
      return false;
    }

    const trigger = value as {
      mouseButton?: unknown;
      modifiers?: {
        alt?: unknown;
        ctrl?: unknown;
        shift?: unknown;
        meta?: unknown;
      };
    };

    return !!(
      (trigger.mouseButton === "left" ||
        trigger.mouseButton === "middle" ||
        trigger.mouseButton === "right") &&
      trigger.modifiers &&
      typeof trigger.modifiers.alt === "boolean" &&
      typeof trigger.modifiers.ctrl === "boolean" &&
      typeof trigger.modifiers.shift === "boolean" &&
      typeof trigger.modifiers.meta === "boolean"
    );
  }
}





