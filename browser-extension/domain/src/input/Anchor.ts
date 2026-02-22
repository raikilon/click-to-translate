export interface AnchorPoint {
  kind: "point";
  x: number;
  y: number;
}

export interface AnchorRect {
  kind: "rect";
  x: number;
  y: number;
  w: number;
  h: number;
}

export type Anchor = AnchorPoint | AnchorRect;
