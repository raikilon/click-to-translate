/**
 * File role: Wraps browser.identity APIs behind a small port used by auth flow.
 * Why this shape: Identity behavior varies across extension contexts, so this isolates browser API specifics.
 */
export interface IdentityPort {
  launchWebAuthFlow(url: string, interactive?: boolean): Promise<string>;
  getRedirectUrl(path?: string): string;
}

class BrowserIdentityAdapter implements IdentityPort {
  private readonly identityApi: {
    launchWebAuthFlow?: (details: {
      url: string;
      interactive?: boolean;
    }) => Promise<string | undefined>;
    getRedirectURL?: (path?: string) => string;
  };

  constructor() {
    if (!browser.identity) {
      throw new Error("browser.identity API is not available in this build.");
    }

    this.identityApi = browser.identity as {
      launchWebAuthFlow?: (details: {
        url: string;
        interactive?: boolean;
      }) => Promise<string | undefined>;
      getRedirectURL?: (path?: string) => string;
    };
  }

  async launchWebAuthFlow(url: string, interactive = true): Promise<string> {
    if (typeof this.identityApi.launchWebAuthFlow !== "function") {
      throw new Error("browser.identity.launchWebAuthFlow is not available.");
    }

    const redirectUrl = await this.identityApi.launchWebAuthFlow({
      url,
      interactive,
    });
    if (!redirectUrl) {
      throw new Error("No redirect URL returned by launchWebAuthFlow.");
    }

    return redirectUrl;
  }

  getRedirectUrl(path?: string): string {
    if (typeof this.identityApi.getRedirectURL !== "function") {
      throw new Error("browser.identity.getRedirectURL is not available.");
    }

    return this.identityApi.getRedirectURL(path);
  }
}

export const extensionIdentity = new BrowserIdentityAdapter();
