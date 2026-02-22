import type { RenderPayload, Renderer } from "@application";
import type { DisplayInstruction } from "@domain";
import { tooltipRenderer } from "./tooltipRenderer";
import { videoOverlayRenderer } from "./videoOverlayRenderer";

export class ContentRendererBridge implements Renderer {
  async render(instruction: DisplayInstruction, payload: RenderPayload): Promise<void> {
    if (instruction.mode === "POPUP_ONLY") {
      return;
    }

    if (instruction.mode === "TOOLTIP") {
      tooltipRenderer.render(instruction, payload);
      return;
    }

    if (instruction.mode === "VIDEO_OVERLAY") {
      videoOverlayRenderer.show(instruction.anchor, payload.text, {
        dismissOnOutsideClick: instruction.dismissOn?.outsideClick,
        dismissOnEscape: instruction.dismissOn?.escape,
      });
    }
  }

  async dismiss(): Promise<void> {
    tooltipRenderer.dismiss();
    videoOverlayRenderer.hide();
  }
}
