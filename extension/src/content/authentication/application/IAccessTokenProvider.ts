export interface IAccessTokenProvider {
  getAccessToken(interactive?: boolean): Promise<string | null>;
}
