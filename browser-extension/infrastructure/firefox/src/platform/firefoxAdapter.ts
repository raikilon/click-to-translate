import type { BrowserAdapter, IdentityPort } from "@infra-shared";

function resolveIdentityApi(): IdentityPort {
  if (typeof browser === "undefined" || !browser.identity) {
    throw new Error("browser.identity API is not available in this Firefox build.");
  }

  const identityApi = browser.identity as {
    launchWebAuthFlow?: (details: {
      url: string;
      interactive?: boolean;
    }) => Promise<string | undefined>;
    getRedirectURL?: (path?: string) => string;
  };

  return {
    async launchWebAuthFlow(url: string): Promise<string> {
      if (typeof identityApi.launchWebAuthFlow !== "function") {
        throw new Error("browser.identity.launchWebAuthFlow is not available.");
      }

      const redirectUrl = await identityApi.launchWebAuthFlow({
        url,
        interactive: true,
      });

      if (!redirectUrl) {
        throw new Error("No redirect URL returned by launchWebAuthFlow.");
      }

      return redirectUrl;
    },
    getRedirectUrl(path?: string): string {
      if (typeof identityApi.getRedirectURL !== "function") {
        throw new Error("browser.identity.getRedirectURL is not available.");
      }

      return identityApi.getRedirectURL(path);
    },
  };
}

export const firefoxAdapter: BrowserAdapter = {
  runtime: {
    sendMessage<TResponse>(message: unknown): Promise<TResponse> {
      return browser.runtime.sendMessage(message) as Promise<TResponse>;
    },
    addMessageListener(handler): void {
      browser.runtime.onMessage.addListener((rawMessage, sender) =>
        handler(rawMessage, { tabId: sender.tab?.id }),
      );
    },
  },
  storage: {
    get<T>(key: string): Promise<T | undefined> {
      return browser.storage.local.get(key).then((items) => items[key] as T | undefined);
    },
    set<T>(key: string, value: T): Promise<void> {
      return browser.storage.local.set({ [key]: value });
    },
    remove(key: string): Promise<void> {
      return browser.storage.local.remove(key);
    },
  },
  identity: resolveIdentityApi(),
  nowMs(): number {
    return Date.now();
  },
};
