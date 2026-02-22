import type { AuthSession } from "../model/AuthSession";

export interface AuthSessionStore {
  get(): Promise<AuthSession | null>;
  set(session: AuthSession): Promise<void>;
  clear(): Promise<void>;
}
