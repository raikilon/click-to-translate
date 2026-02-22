import type { ApiClient } from "../contracts/ApiClient";
import type { AuthFlow } from "../contracts/AuthFlow";
import type { AuthSessionStore } from "../contracts/AuthSessionStore";
import type { Clock } from "../contracts/Clock";
import type { AuthSession } from "../model/AuthSession";

const DEFAULT_EXPIRY_SKEW_MS = 30_000;

export interface EnsureAuthSessionOptions {
  interactive?: boolean;
  expirySkewMs?: number;
}

export class EnsureAuthSessionUseCase {
  constructor(
    private readonly authSessionStore: AuthSessionStore,
    private readonly authFlow: AuthFlow,
    private readonly apiClient: ApiClient,
    private readonly clock: Clock,
  ) {}

  async execute(
    options: EnsureAuthSessionOptions = {},
  ): Promise<AuthSession | null> {
    const expirySkewMs = options.expirySkewMs ?? DEFAULT_EXPIRY_SKEW_MS;
    const session = await this.authSessionStore.get();

    if (session && this.isSessionUsable(session, expirySkewMs)) {
      return session;
    }

    if (session?.refreshToken) {
      try {
        const refreshedSession = await this.apiClient.refreshAccessToken(
          session.refreshToken,
        );
        await this.authSessionStore.set(refreshedSession);
        return refreshedSession;
      } catch {
        await this.authSessionStore.clear();
      }
    } else if (session) {
      await this.authSessionStore.clear();
    }

    if (!options.interactive) {
      return null;
    }

    const interactiveSession = await this.authFlow.loginInteractive();
    await this.authSessionStore.set(interactiveSession);
    return interactiveSession;
  }

  private isSessionUsable(session: AuthSession, expirySkewMs: number): boolean {
    return session.expiresAtMs - expirySkewMs > this.clock.nowMs();
  }
}
