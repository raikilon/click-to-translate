export type TriggerMouseButton = "left" | "middle" | "right";

export interface TriggerModifiers {
  alt: boolean;
  ctrl: boolean;
  shift: boolean;
  meta: boolean;
}

export interface TriggerPrefs {
  mouseButton: TriggerMouseButton;
  modifiers: TriggerModifiers;
}

export const DEFAULT_TRIGGER_PREFS: TriggerPrefs = {
  mouseButton: "left",
  modifiers: {
    alt: true,
    ctrl: false,
    shift: false,
    meta: false,
  },
};
