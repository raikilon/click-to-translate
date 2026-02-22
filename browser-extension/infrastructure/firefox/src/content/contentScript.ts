import { registerContentScript } from "@infra-shared/content/contentScript";
import { firefoxAdapter } from "../platform/firefoxAdapter";
import { collectSnapshots } from "./probes/probeResolver";
import { ContentRendererBridge } from "./render/rendererBridge";

registerContentScript({
  runtime: firefoxAdapter.runtime,
  renderer: new ContentRendererBridge(),
  collectSnapshots,
  nowMs: firefoxAdapter.nowMs,
});
