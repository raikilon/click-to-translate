import type { DisplayInstruction } from "../../../domain/src";

export interface RenderPayload {
  text: string;
  debug?: unknown;
}

export interface Renderer {
  render(instruction: DisplayInstruction, payload: RenderPayload): Promise<void>;
  dismiss(): Promise<void>;
}
