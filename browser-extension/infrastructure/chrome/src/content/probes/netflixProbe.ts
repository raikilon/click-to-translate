import type { SubtitleSnapshot, Trigger } from "@domain";
import {
  getSubtitleSnapshotByPoint,
  type PointOffset,
  type PointSubtitleProbeConfig,
} from "@infra-shared/content/probes/pointSubtitleProbe";

const NEARBY_POINT_OFFSETS: readonly PointOffset[] = [
  { x: 0, y: 0 },
  { x: 0, y: -12 },
  { x: 0, y: 12 },
  { x: -12, y: 0 },
  { x: 12, y: 0 },
];

function isNetflixWatchUrl(url: string): boolean {
  return url.includes("netflix.com/watch");
}

const NETFLIX_SUBTITLE_CONFIG: PointSubtitleProbeConfig = {
  provider: "netflix",
  isWatchUrl: isNetflixWatchUrl,
  subtitleSelectors: [
    '[class*="player-timedtext"]',
    '[class*="timedtext"]',
    '[class*="subtitle"]',
    '[class*="subtitles"]',
    '[class*="caption"]',
    '[id*="timedtext"]',
    '[id*="subtitle"]',
    '[data-uia*="timedtext"]',
    '[data-uia*="subtitle"]',
    '[aria-live]',
  ],
  videoRootSelectors: [
    '[data-uia="watch-video"]',
    ".watch-video",
    ".watch-video--player-view",
    ".nf-player-container",
  ],
  subtitleHints: ["timedtext", "subtitle", "caption"],
  maxPointElements: 14,
  maxAncestorDepth: 7,
  selectorCandidateLimit: 120,
  pointMarginPx: 20,
  nearbyPointOffsets: NEARBY_POINT_OFFSETS,
};

export async function getSubtitleSnapshot(
  trigger: Trigger,
): Promise<SubtitleSnapshot | null> {
  return getSubtitleSnapshotByPoint(trigger, NETFLIX_SUBTITLE_CONFIG);
}
