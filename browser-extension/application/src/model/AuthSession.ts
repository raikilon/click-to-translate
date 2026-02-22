export interface AuthSession {
  accessToken: string;
  refreshToken?: string;
  expiresAtMs: number;
  idToken?: string;
  tokenType?: "Bearer";
}
