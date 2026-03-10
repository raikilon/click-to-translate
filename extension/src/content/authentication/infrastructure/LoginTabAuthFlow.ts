interface PendingLoginTab {
  gatewayOrigin: string;
  leftGatewayOrigin: boolean;
}

export class LoginTabAuthFlow {
  private readonly pendingLoginTabsById = new Map<number, PendingLoginTab>();
  private listenersRegistered = false;

  private readonly onLoginTabUpdated: Parameters<typeof browser.tabs.onUpdated.addListener>[0] = (
    tabId,
    changeInfo,
    tab,
  ) => {
    this.handleLoginTabUpdated(tabId, changeInfo.url ?? tab.url);
  };

  private readonly onLoginTabRemoved: Parameters<typeof browser.tabs.onRemoved.addListener>[0] = (
    tabId,
  ) => {
    this.pendingLoginTabsById.delete(tabId);
    this.unregisterLoginTabListenersIfIdle();
  };

  async start(loginUrl: string, gatewayOrigin: string): Promise<void> {
    if (!browser.tabs?.create || !browser.tabs?.remove || !browser.tabs?.onUpdated || !browser.tabs?.onRemoved) {
      throw new Error("browser.tabs APIs are not available in this build.");
    }

    this.registerLoginTabListeners();

    let createdTabId: number | undefined;
    try {
      const createdTab = await browser.tabs.create({
        url: loginUrl,
        active: true,
      });
      createdTabId = createdTab.id;
    } catch (error) {
      this.unregisterLoginTabListenersIfIdle();
      throw error;
    }

    if (typeof createdTabId !== "number") {
      this.unregisterLoginTabListenersIfIdle();
      throw new Error("Unable to identify login tab.");
    }

    this.pendingLoginTabsById.set(createdTabId, {
      gatewayOrigin,
      leftGatewayOrigin: false,
    });
  }

  private registerLoginTabListeners(): void {
    if (this.listenersRegistered) {
      return;
    }

    browser.tabs.onUpdated.addListener(this.onLoginTabUpdated);
    browser.tabs.onRemoved.addListener(this.onLoginTabRemoved);
    this.listenersRegistered = true;
  }

  private unregisterLoginTabListenersIfIdle(): void {
    if (!this.listenersRegistered || this.pendingLoginTabsById.size > 0) {
      return;
    }

    browser.tabs.onUpdated.removeListener(this.onLoginTabUpdated);
    browser.tabs.onRemoved.removeListener(this.onLoginTabRemoved);
    this.listenersRegistered = false;
  }

  private handleLoginTabUpdated(tabId: number, url: string | undefined): void {
    const pendingLoginTab = this.pendingLoginTabsById.get(tabId);
    if (!pendingLoginTab || !url) {
      return;
    }

    const parsedUrl = this.tryParseUrl(url);
    if (!parsedUrl) {
      return;
    }

    if (parsedUrl.origin !== pendingLoginTab.gatewayOrigin) {
      pendingLoginTab.leftGatewayOrigin = true;
      return;
    }

    if (!pendingLoginTab.leftGatewayOrigin || !this.isGatewayLoginCompletionPath(parsedUrl.pathname)) {
      return;
    }

    void this.closeLoginTab(tabId);
  }

  private isGatewayLoginCompletionPath(pathname: string): boolean {
    return pathname.startsWith("/login/oauth2/code/") || pathname === "/";
  }

  private async closeLoginTab(tabId: number): Promise<void> {
    this.pendingLoginTabsById.delete(tabId);
    try {
      await browser.tabs.remove(tabId);
    } finally {
      this.unregisterLoginTabListenersIfIdle();
    }
  }

  private tryParseUrl(url: string): URL | null {
    try {
      return new URL(url);
    } catch {
      return null;
    }
  }
}
