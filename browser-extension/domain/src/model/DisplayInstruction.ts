import type { Anchor } from "../input/Anchor";

export interface DisplayInstruction {
  mode: "TOOLTIP" | "VIDEO_OVERLAY" | "POPUP_ONLY";
  anchor: Anchor;
  dismissOn?: {
    outsideClick?: boolean;
    escape?: boolean;
    scroll?: boolean;
  };
}
