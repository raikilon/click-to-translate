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

function isYouTubeWatchUrl(url: string): boolean {
  return url.includes("youtube.com/watch") || url.includes("youtu.be/");
}

const YOUTUBE_SUBTITLE_CONFIG: PointSubtitleProbeConfig = {
  provider: "youtube",
  isWatchUrl: isYouTubeWatchUrl,
  subtitleSelectors: [
    ".ytp-caption-window-container",
    ".ytp-caption-window",
    ".ytp-caption-window-bottom",
    ".ytp-caption-window-top",
    ".caption-window",
    ".captions-text",
    ".ytp-caption-segment",
    '[class*="ytp-caption"]',
    '[class*="caption-window"]',
    '[class*="captions-text"]',
    '[class*="caption"]',
    '[id*="caption"]',
  ],
  videoRootSelectors: [
    "#movie_player",
    ".html5-video-player",
    "ytd-player",
    "#player",
  ],
  subtitleHints: ["ytp-caption", "caption-window", "captions-text"],
  maxPointElements: 14,
  maxAncestorDepth: 6,
  selectorCandidateLimit: 80,
  pointMarginPx: 20,
  nearbyPointOffsets: NEARBY_POINT_OFFSETS,
};

export async function getSubtitleSnapshot(
  trigger: Trigger,
): Promise<SubtitleSnapshot | null> {
  return getSubtitleSnapshotByPoint(trigger, YOUTUBE_SUBTITLE_CONFIG);
}
