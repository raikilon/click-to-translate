import type { Snapshots, Trigger } from "@domain";
import { getTextAtPoint } from "./textAtPointProbe";

function detectProvider(url: string): "youtube" | "netflix" | "unknown" {
  if (url.includes("youtube.com/watch") || url.includes("youtu.be/")) {
    return "youtube";
  }

  if (url.includes("netflix.com/watch")) {
    return "netflix";
  }

  return "unknown";
}

function getSelectionSnapshot(trigger: Trigger): Snapshots["selection"] {
  const selectedText = window.getSelection()?.toString().trim();
  if (!selectedText) {
    return null;
  }

  return {
    selectedText,
    anchor: {
      kind: "point",
      x: trigger.mouse.x,
      y: trigger.mouse.y,
    },
  };
}

export function collectGenericSnapshots(trigger: Trigger): Snapshots {
  const pageUrl = window.location.href;

  return {
    pageInfo: {
      url: pageUrl,
      title: document.title || undefined,
      hostname: window.location.hostname || undefined,
      provider: detectProvider(pageUrl),
    },
    selection: getSelectionSnapshot(trigger),
    textAtPoint: getTextAtPoint(trigger),
    subtitle: null,
  };
}
