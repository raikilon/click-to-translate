import type { CapturedClick } from "@domain";

export interface CapturePointInput {
  x: number;
  y: number;
}

export interface PointCapture {
  capture(input: CapturePointInput): CapturedClick | null;
}
