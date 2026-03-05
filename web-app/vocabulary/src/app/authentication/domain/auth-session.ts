export interface AuthSession {
  accessToken: string;
  accessTokenExpiresAtMs: number;
  refreshToken: string;
  refreshTokenExpiresAtMs: number;
  idToken?: string;
}
