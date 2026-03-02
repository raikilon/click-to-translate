import type { AuthState } from "@/content/authentication/domain/AuthState";

export interface IAuthenticationService {
  getAuthState(): Promise<AuthState>;
  login(): Promise<AuthState>;
  logout(): Promise<void>;
  getAccessToken(interactive?: boolean): Promise<string | null>;
}
