import type {
  LanguagePrefs,
  TriggerMouseButton,
  TriggerPrefs,
} from "@domain";

type OptionsStatusKind = "error" | "ok";

export class OptionsView {
  bindOptionsFormSubmit(listener: (event: Event) => void): void {
    this.byId<HTMLFormElement>("optionsForm").addEventListener("submit", listener);
  }

  setStatus(message: string, isError = false): void {
    const status = this.byId<HTMLDivElement>("status");
    const kind: OptionsStatusKind = isError ? "error" : "ok";
    status.textContent = message;
    status.dataset.kind = kind;
  }

  readLanguagePrefsFromForm(): LanguagePrefs {
    const sourceLanguageId = this.byId<HTMLInputElement>("sourceLanguageId").value;
    const targetLanguageId = this.byId<HTMLInputElement>("targetLanguageId").value;

    return {
      sourceLanguageId: sourceLanguageId || undefined,
      targetLanguageId: targetLanguageId || undefined,
    };
  }

  fillLanguagePrefsForm(prefs: LanguagePrefs): void {
    const sourceLanguageInput = this.byId<HTMLInputElement>("sourceLanguageId");
    const targetLanguageInput = this.byId<HTMLInputElement>("targetLanguageId");

    if (prefs.sourceLanguageId !== undefined) {
      sourceLanguageInput.value = prefs.sourceLanguageId;
    }

    if (prefs.targetLanguageId !== undefined) {
      targetLanguageInput.value = prefs.targetLanguageId;
    }
  }

  readTriggerPrefsFromForm(): TriggerPrefs {
    return {
      mouseButton: this.byId<HTMLSelectElement>("mouseButton").value as TriggerMouseButton,
      modifiers: {
        alt: this.byId<HTMLInputElement>("modAlt").checked,
        ctrl: this.byId<HTMLInputElement>("modCtrl").checked,
        shift: this.byId<HTMLInputElement>("modShift").checked,
        meta: this.byId<HTMLInputElement>("modMeta").checked,
      },
    };
  }

  fillTriggerPrefsForm(prefs: TriggerPrefs): void {
    this.byId<HTMLSelectElement>("mouseButton").value = prefs.mouseButton;
    this.byId<HTMLInputElement>("modAlt").checked = prefs.modifiers.alt;
    this.byId<HTMLInputElement>("modCtrl").checked = prefs.modifiers.ctrl;
    this.byId<HTMLInputElement>("modShift").checked = prefs.modifiers.shift;
    this.byId<HTMLInputElement>("modMeta").checked = prefs.modifiers.meta;
  }

  private byId<T extends HTMLElement>(id: string): T {
    const element = document.getElementById(id);
    if (!element) {
      throw new Error(`Missing options element: ${id}`);
    }

    return element as T;
  }
}
