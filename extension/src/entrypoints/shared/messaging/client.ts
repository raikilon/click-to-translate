import {
  sendBackgroundMessage,
  SERVICES,
  type BackgroundPayload,
  type BackgroundResponse,
} from "./services";

export interface BackgroundClient {
  translateAtPoint(
    capture: BackgroundPayload<"translateAtPoint">["capture"],
  ): Promise<BackgroundResponse<"translateAtPoint">>;
  login(): Promise<BackgroundResponse<"login">>;
  logout(): Promise<BackgroundResponse<"logout">>;
  getAuthState(): Promise<BackgroundResponse<"getAuthState">>;
}

class MessagingBackgroundClient implements BackgroundClient {
  translateAtPoint(
    capture: BackgroundPayload<"translateAtPoint">["capture"],
  ): Promise<BackgroundResponse<"translateAtPoint">> {
    return sendBackgroundMessage(SERVICES.translateAtPoint, {
      capture,
    });
  }

  login(): Promise<BackgroundResponse<"login">> {
    return sendBackgroundMessage(SERVICES.login, {});
  }

  logout(): Promise<BackgroundResponse<"logout">> {
    return sendBackgroundMessage(SERVICES.logout, {});
  }

  getAuthState(): Promise<BackgroundResponse<"getAuthState">> {
    return sendBackgroundMessage(SERVICES.getAuthState, {});
  }
}

export class BackgroundClientFactory {
  static create(): BackgroundClient {
    return new MessagingBackgroundClient();
  }
}
