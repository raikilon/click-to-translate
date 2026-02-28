export interface SubtitleSiteProfile {
  subtitleSelectors: readonly string[];
  matches(url: string): boolean;
}

const youtubeSubtitleProfile: SubtitleSiteProfile = {
  subtitleSelectors: [
    ".ytp-caption-window-container",
    ".ytp-caption-window",
    ".caption-window",
    ".captions-text",
    ".ytp-caption-segment",
    '[class*="ytp-caption"]',
    '[class*="caption-window"]',
    '[class*="captions-text"]',
  ],
  matches(url: string): boolean {
    return url.includes("youtube.com/watch") || url.includes("youtu.be/");
  },
};

const netflixSubtitleProfile: SubtitleSiteProfile = {
  subtitleSelectors: [
    '[class*="player-timedtext"]',
    '[class*="timedtext"]',
    '[class*="subtitle"]',
    '[class*="subtitles"]',
    '[class*="caption"]',
    '[data-uia*="timedtext"]',
    '[data-uia*="subtitle"]',
    '[aria-live]',
  ],
  matches(url: string): boolean {
    return url.includes("netflix.com/watch");
  },
};

const subtitleSiteProfiles: readonly SubtitleSiteProfile[] = [
  youtubeSubtitleProfile,
  netflixSubtitleProfile,
];

export function resolveSubtitleSiteProfile(
  url: string,
): SubtitleSiteProfile | undefined {
  return subtitleSiteProfiles.find((profile) => profile.matches(url));
}
