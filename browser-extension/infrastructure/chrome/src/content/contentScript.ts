import { registerContentScript } from "@infra-shared/content/contentScript";
import { chromeAdapter } from "../platform/chromeAdapter";
import { collectSnapshots } from "./probes/probeResolver";
import { ContentRendererBridge } from "./render/rendererBridge";

registerContentScript({
  runtime: chromeAdapter.runtime,
  renderer: new ContentRendererBridge(),
  collectSnapshots,
  nowMs: chromeAdapter.nowMs,
});
