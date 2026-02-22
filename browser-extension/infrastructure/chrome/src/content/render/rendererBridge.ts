import type { RenderPayload, Renderer } from "@application";
import type { DisplayInstruction } from "@domain";
import { tooltipRenderer } from "./tooltipRenderer";

export class ContentRendererBridge implements Renderer {
  async render(instruction: DisplayInstruction, payload: RenderPayload): Promise<void> {
    if (instruction.mode === "POPUP_ONLY") {
      return;
    }

    // MVP: render all non-popup instructions as tooltip.
    tooltipRenderer.render(instruction, payload);
  }

  async dismiss(): Promise<void> {
    tooltipRenderer.dismiss();
  }
}
