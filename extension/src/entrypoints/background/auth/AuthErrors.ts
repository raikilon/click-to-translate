export const AUTH_REQUIRED_MESSAGE = "Login required. Open the extension popup to sign in.";
export const AUTH_SESSION_EXPIRED_MESSAGE =
  "Session expired. Open the extension popup to sign in again.";

export class AuthRequiredError extends Error {
  constructor(message = AUTH_REQUIRED_MESSAGE) {
    super(message);
    this.name = "AuthRequiredError";
  }
}

export class AuthSessionExpiredError extends AuthRequiredError {
  constructor() {
    super(AUTH_SESSION_EXPIRED_MESSAGE);
    this.name = "AuthSessionExpiredError";
  }
}
