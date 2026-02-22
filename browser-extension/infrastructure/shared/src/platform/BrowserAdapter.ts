export interface RuntimeMessageSender {
  tabId?: number;
}

export interface RuntimePort {
  sendMessage<TResponse>(message: unknown): Promise<TResponse>;
  addMessageListener(
    handler: (
      rawMessage: unknown,
      sender: RuntimeMessageSender,
    ) => Promise<unknown>,
  ): void;
}

export interface StoragePort {
  get<T>(key: string): Promise<T | undefined>;
  set<T>(key: string, value: T): Promise<void>;
  remove(key: string): Promise<void>;
}

export interface IdentityPort {
  launchWebAuthFlow(url: string): Promise<string>;
  getRedirectUrl(path?: string): string;
}

export interface BrowserAdapter {
  runtime: RuntimePort;
  storage: StoragePort;
  identity: IdentityPort;
  nowMs(): number;
}
