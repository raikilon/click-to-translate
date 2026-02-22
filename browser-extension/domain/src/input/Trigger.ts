export interface Trigger {
  url: string;
  mouse: {
    button: "left" | "middle" | "right";
    x: number;
    y: number;
  };
  modifiers: {
    alt: boolean;
    ctrl: boolean;
    shift: boolean;
    meta: boolean;
  };
  selectedText?: string;
  occurredAtMs: number;
}
