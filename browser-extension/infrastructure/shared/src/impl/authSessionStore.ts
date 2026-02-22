import type { AuthSession, AuthSessionStore } from "@application";
import type { StoragePort } from "../platform/BrowserAdapter";

const SESSION_KEY = "authSession";

function normalizeAuthSession(candidate: unknown): AuthSession | null {
  if (!candidate || typeof candidate !== "object") {
    return null;
  }

  const value = candidate as Partial<AuthSession>;
  if (typeof value.accessToken !== "string" || typeof value.expiresAtMs !== "number") {
    return null;
  }

  return {
    accessToken: value.accessToken,
    expiresAtMs: value.expiresAtMs,
    refreshToken: typeof value.refreshToken === "string" ? value.refreshToken : undefined,
    idToken: typeof value.idToken === "string" ? value.idToken : undefined,
    tokenType: value.tokenType === "Bearer" ? "Bearer" : undefined,
  };
}

export class ExtensionAuthSessionStore implements AuthSessionStore {
  constructor(private readonly storage: StoragePort) {}

  async get(): Promise<AuthSession | null> {
    const rawSession = await this.storage.get<unknown>(SESSION_KEY);
    return normalizeAuthSession(rawSession);
  }

  async set(session: AuthSession): Promise<void> {
    const normalized = normalizeAuthSession(session);
    if (!normalized) {
      throw new Error("Cannot store invalid auth session.");
    }

    await this.storage.set(SESSION_KEY, normalized);
  }

  async clear(): Promise<void> {
    await this.storage.remove(SESSION_KEY);
  }
}
