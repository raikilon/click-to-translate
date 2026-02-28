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
const subtitleSiteProfiles: readonly SubtitleSiteProfile[] = [youtubeSubtitleProfile];

export function resolveSubtitleSiteProfile(
  url: string,
): SubtitleSiteProfile | undefined {
  return subtitleSiteProfiles.find((profile) => profile.matches(url));
}
