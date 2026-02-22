import type { Snapshots, Trigger } from "@domain";
import { collectGenericSnapshots } from "./genericPageProbe";
import {
  getSubtitleSnapshot as getNetflixSubtitleSnapshot,
  startNetflixSubtitleObserver,
  stopNetflixSubtitleObserver,
} from "./netflixProbe";
import {
  getSubtitleSnapshot as getYouTubeSubtitleSnapshot,
  startYouTubeSubtitleObserver,
  stopYouTubeSubtitleObserver,
} from "./youtubeProbe";

function isYouTubeWatchUrl(url: string): boolean {
  return url.includes("youtube.com/watch") || url.includes("youtu.be/");
}

function isNetflixWatchUrl(url: string): boolean {
  return url.includes("netflix.com/watch");
}

export async function collectSnapshots(trigger: Trigger): Promise<Snapshots> {
  const genericSnapshots = collectGenericSnapshots(trigger);

  if (isYouTubeWatchUrl(trigger.url)) {
    stopNetflixSubtitleObserver();
    startYouTubeSubtitleObserver();
    const subtitle = await getYouTubeSubtitleSnapshot(trigger);

    return {
      ...genericSnapshots,
      subtitle: subtitle ?? null,
    };
  }

  if (isNetflixWatchUrl(trigger.url)) {
    stopYouTubeSubtitleObserver();
    startNetflixSubtitleObserver();
    const subtitle = await getNetflixSubtitleSnapshot(trigger);

    return {
      ...genericSnapshots,
      subtitle: subtitle ?? null,
    };
  }

  stopYouTubeSubtitleObserver();
  stopNetflixSubtitleObserver();
  return genericSnapshots;
}
