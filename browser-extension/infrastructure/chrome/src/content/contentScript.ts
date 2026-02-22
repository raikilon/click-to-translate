import { registerContentScript } from "@infra-shared/content/contentScript";
import { chromeAdapter } from "../platform/chromeAdapter";
import { collectSnapshots } from "./probes/probeResolver";
import { ContentRendererBridge } from "./render/rendererBridge";

registerContentScript({
  runtime: chromeAdapter.runtime,
  storage: chromeAdapter.storage,
  renderer: new ContentRendererBridge(),
  collectSnapshots,
  nowMs: chromeAdapter.nowMs,
});
