import type { AuthSession, AuthSessionStore } from "@application";

const SESSION_KEY = "authSession";

function getStorageLocal<T>(key: string): Promise<T | undefined> {
  return browser.storage.local
    .get(key)
    .then((items) => items[key] as T | undefined);
}

function setStorageLocal<T>(key: string, value: T): Promise<void> {
  return browser.storage.local.set({ [key]: value });
}

function removeStorageLocal(key: string): Promise<void> {
  return browser.storage.local.remove(key);
}

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

export class FirefoxAuthSessionStore implements AuthSessionStore {
  async get(): Promise<AuthSession | null> {
    const rawSession = await getStorageLocal<unknown>(SESSION_KEY);
    return normalizeAuthSession(rawSession);
  }

  async set(session: AuthSession): Promise<void> {
    const normalized = normalizeAuthSession(session);
    if (!normalized) {
      throw new Error("Cannot store invalid auth session.");
    }

    await setStorageLocal(SESSION_KEY, normalized);
  }

  async clear(): Promise<void> {
    await removeStorageLocal(SESSION_KEY);
  }
}
