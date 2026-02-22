import type { Snapshots, Trigger } from "@domain";
import { collectGenericSnapshots } from "./genericPageProbe";
import {
  getSubtitleSnapshot,
  startYouTubeSubtitleObserver,
  stopYouTubeSubtitleObserver,
} from "./youtubeProbe";

function isYouTubeWatchUrl(url: string): boolean {
  return url.includes("youtube.com/watch") || url.includes("youtu.be/");
}

export async function collectSnapshots(trigger: Trigger): Promise<Snapshots> {
  const genericSnapshots = collectGenericSnapshots(trigger);

  if (!isYouTubeWatchUrl(trigger.url)) {
    stopYouTubeSubtitleObserver();
    return genericSnapshots;
  }

  startYouTubeSubtitleObserver();
  const subtitle = await getSubtitleSnapshot(trigger);

  return {
    ...genericSnapshots,
    subtitle: subtitle ?? null,
  };
}

