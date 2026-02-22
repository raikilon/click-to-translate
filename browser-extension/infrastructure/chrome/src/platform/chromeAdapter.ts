import type { BrowserAdapter } from "@infra-shared/platform/BrowserAdapter";

function toErrorPayload(error: unknown): { ok: false; error: string } {
  return {
    ok: false,
    error: error instanceof Error ? error.message : "Unknown error",
  };
}

export const chromeAdapter: BrowserAdapter = {
  runtime: {
    sendMessage<TResponse>(message: unknown): Promise<TResponse> {
      return new Promise((resolve, reject) => {
        chrome.runtime.sendMessage(message, (response?: TResponse) => {
          if (chrome.runtime.lastError) {
            reject(new Error(chrome.runtime.lastError.message));
            return;
          }

          if (response === undefined) {
            reject(new Error("Background did not return a response."));
            return;
          }

          resolve(response);
        });
      });
    },
    addMessageListener(handler): void {
      chrome.runtime.onMessage.addListener((rawMessage, sender, sendResponse) => {
        void handler(rawMessage, { tabId: sender.tab?.id })
          .then((response) => sendResponse(response))
          .catch((error: unknown) => sendResponse(toErrorPayload(error)));

        return true;
      });
    },
  },
  storage: {
    get<T>(key: string): Promise<T | undefined> {
      return new Promise((resolve) => {
        chrome.storage.local.get([key], (items) => {
          resolve(items[key] as T | undefined);
        });
      });
    },
    set<T>(key: string, value: T): Promise<void> {
      return new Promise((resolve) => {
        chrome.storage.local.set({ [key]: value }, () => resolve());
      });
    },
    remove(key: string): Promise<void> {
      return new Promise((resolve) => {
        chrome.storage.local.remove(key, () => resolve());
      });
    },
  },
  identity: {
    launchWebAuthFlow(url: string): Promise<string> {
      return new Promise((resolve, reject) => {
        chrome.identity.launchWebAuthFlow(
          {
            url,
            interactive: true,
          },
          (redirectUrl) => {
            if (chrome.runtime.lastError) {
              reject(new Error(chrome.runtime.lastError.message));
              return;
            }

            if (!redirectUrl) {
              reject(new Error("No redirect URL returned by launchWebAuthFlow."));
              return;
            }

            resolve(redirectUrl);
          },
        );
      });
    },
    getRedirectUrl(path?: string): string {
      return chrome.identity.getRedirectURL(path);
    },
  },
  nowMs(): number {
    return Date.now();
  },
};
