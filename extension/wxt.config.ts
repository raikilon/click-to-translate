/**
 * File role: Configures WXT build + manifest generation for MV3 targets.
 * Why this shape: WXT-specific settings live here so browser differences stay in config, not scattered in feature code.
 */
import { defineConfig } from "wxt";

export default defineConfig({
  srcDir: "src",
  manifestVersion: 3,
  alias: {
    "@domain": "src/domain",
    "@application": "src/application",
    "@infra": "src/infrastructure",
  },
  manifest: ({ browser }) => ({
    name: "Click to Translate",
    description: "Translate clicked text and save translated segments.",
    version: "0.1.0",
    icons: {
      "16": "icon/16.png",
      "32": "icon/32.png",
      "48": "icon/48.png",
      "96": "icon/96.png",
      "128": "icon/128.png",
    },
    permissions: ["storage", "identity"],
    host_permissions: ["http://*/*", "https://*/*"],
    browser_specific_settings:
      browser === "firefox"
        ? {
            gecko: {
              id: "click-to-translate@local",
              strict_min_version: "121.0",
            },
          }
        : undefined,
  }),
});
