import { GenericStrategy } from "./generic/GenericStrategy";
import { NetflixStrategy } from "./netflix/NetflixStrategy";
import type { Strategy, StrategyId } from "./Strategy";
import { YouTubeStrategy } from "./youtube/YouTubeStrategy";

export interface StrategyOverride {
  pattern: string;
  strategyId: StrategyId;
}

function buildDefaultStrategies(): Strategy[] {
  return [new YouTubeStrategy(), new NetflixStrategy(), new GenericStrategy()];
}

function escapeRegex(value: string): string {
  return value.replace(/[.+?^${}()|[\]\\]/g, "\\$&");
}

function patternMatchesUrl(pattern: string, url: string): boolean {
  const normalizedPattern = pattern.trim();
  if (!normalizedPattern) {
    return false;
  }

  if (normalizedPattern.includes("*")) {
    const wildcardAsRegex = escapeRegex(normalizedPattern).replace(/\*/g, ".*");
    return new RegExp(`^${wildcardAsRegex}$`).test(url);
  }

  return url.includes(normalizedPattern);
}

export class StrategyResolver {
  private readonly strategies: Strategy[];
  private readonly byId: Map<StrategyId, Strategy>;

  constructor(strategies: Strategy[] = buildDefaultStrategies()) {
    this.strategies =
      strategies.length > 0 ? strategies : buildDefaultStrategies();
    this.byId = new Map(this.strategies.map((strategy) => [strategy.id, strategy]));
  }

  resolve(url: string, overrides?: StrategyOverride[]): Strategy {
    if (overrides) {
      for (const override of overrides) {
        if (!patternMatchesUrl(override.pattern, url)) {
          continue;
        }

        const overriddenStrategy = this.byId.get(override.strategyId);
        if (overriddenStrategy) {
          return overriddenStrategy;
        }
      }
    }

    const matched = this.strategies.find((strategy) => strategy.matches(url));
    if (matched) {
      return matched;
    }

    return this.byId.get("generic") ?? this.strategies[0];
  }
}
