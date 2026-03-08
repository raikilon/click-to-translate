import { defineConfig } from "wxt";

function resolveGatewayHostPermissions(): string[] {
  const apiBaseUrlRaw = process.env.WXT_API_BASE_URL?.trim();
  if (!apiBaseUrlRaw) {
    throw new Error("Missing required env var: WXT_API_BASE_URL");
  }

  let origin: string;
  try {
    origin = new URL(apiBaseUrlRaw).origin;
  } catch {
    throw new Error("Invalid required env var: WXT_API_BASE_URL");
  }

  return [`${origin}/*`];
}

export default defineConfig({
  srcDir: "src",
  manifestVersion: 3,
  alias: {
    "@infra": "src/infrastructure",
  },
  manifest: ({ browser }) => {
    const gatewayHostPermissions = resolveGatewayHostPermissions();

    return {
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
      permissions: ["storage", "activeTab"],
      host_permissions: gatewayHostPermissions,
      browser_specific_settings:
        browser === "firefox"
          ? {
              gecko: {
                id: "click-to-translate@local",
                strict_min_version: "121.0",
              },
            }
          : undefined,
    };
  },
});
