import type { AuthState } from "@/content/authentication/domain/AuthState";

export interface AuthStateProvider {
  getAuthState(): Promise<AuthState>;
}





