import type { Strategy } from "./Strategy";
import { GenericStrategy } from "./generic/GenericStrategy";
import { NetflixStrategy } from "./netflix/NetflixStrategy";
import { YouTubeStrategy } from "./youtube/YouTubeStrategy";

export type { PageInfo, Snapshots, Strategy, StrategyId } from "./Strategy";
export type { StrategyOverride } from "./StrategyResolver";
export { StrategyResolver } from "./StrategyResolver";
export { GenericStrategy } from "./generic/GenericStrategy";
export { YouTubeStrategy } from "./youtube/YouTubeStrategy";
export { NetflixStrategy } from "./netflix/NetflixStrategy";

export function createDefaultStrategies(): Strategy[] {
  return [new YouTubeStrategy(), new NetflixStrategy(), new GenericStrategy()];
}
