export class AuthRequiredError extends Error {
  constructor(message = 'Login required. Open the extension popup to sign in.') {
    super(message);
    this.name = 'AuthRequiredError';
  }
}

export class AuthSessionExpiredError extends AuthRequiredError {
  constructor() {
    super('Session expired. Open the extension popup to sign in again.');
    this.name = 'AuthSessionExpiredError';
  }
}
