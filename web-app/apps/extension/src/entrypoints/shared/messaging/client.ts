import {
  sendBackgroundMessage,
  SERVICES,
  type BackgroundPayload,
  type BackgroundResponse,
} from "./services";

export interface BackgroundClient {
  translateWord(
    request: BackgroundPayload<"translateWord">["request"],
  ): Promise<BackgroundResponse<"translateWord">>;
  getTranslationLanguages(): Promise<BackgroundResponse<"getTranslationLanguages">>;
  login(): Promise<BackgroundResponse<"login">>;
  logout(): Promise<BackgroundResponse<"logout">>;
  getAuthState(): Promise<BackgroundResponse<"getAuthState">>;
}

class MessagingBackgroundClient implements BackgroundClient {
  translateWord(
    request: BackgroundPayload<"translateWord">["request"],
  ): Promise<BackgroundResponse<"translateWord">> {
    return sendBackgroundMessage(SERVICES.translateWord, {
      request,
    });
  }

  getTranslationLanguages(): Promise<BackgroundResponse<"getTranslationLanguages">> {
    return sendBackgroundMessage(SERVICES.getTranslationLanguages, {});
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





