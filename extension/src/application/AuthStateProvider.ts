export interface AuthState {
  isLoggedIn: boolean;
  expiresAtMs?: number;
}

export interface AuthStateProvider {
  getAuthState(): Promise<AuthState>;
}
