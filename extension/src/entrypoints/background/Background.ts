import {
  TranslateClickUseCase,
} from "@application";
import {
  CapturedClick,
  DomainValidationError,
  FixedWindowReducer,
} from "@domain";
import { onBackgroundMessage, SERVICES } from "../shared/messaging/services";
import { TranslationApi } from "./api/TranslationApi";
import { AuthSessionManager } from "./auth/AuthSessionManager";
import { TranslationGateway } from "./TranslationGateway";
import { LanguagePrefsStoreWxt } from "./storage/LanguagePrefsStoreWxt";

export class Background {
  register(): void {
    const authSessionManager = new AuthSessionManager();
    const languagePrefsStore = new LanguagePrefsStoreWxt();
    const translationApi = new TranslationApi();
    const translationGateway = new TranslationGateway(
      translationApi,
      authSessionManager,
    );

    const translateClick = new TranslateClickUseCase(
      translationGateway,
      languagePrefsStore,
      new FixedWindowReducer(),
    );

    onBackgroundMessage(SERVICES.login, async () => {
      return authSessionManager.login();
    });

    onBackgroundMessage(SERVICES.logout, async () => {
      await authSessionManager.logout();
      return {};
    });

    onBackgroundMessage(SERVICES.getAuthState, async () => {
      return authSessionManager.getAuthState();
    });

    onBackgroundMessage(SERVICES.translateAtPoint, async (message) => {
      try {
        const capturedClick = CapturedClick.hydrate(message.data.capture);
        return translateClick.execute(capturedClick);
      } catch (error) {
        if (error instanceof DomainValidationError) {
          return {
            status: "invalid_capture",
            reason: error.message,
            anchor: message.data.capture.anchor,
          };
        }

        return {
          status: "no_translation",
          reason: error instanceof Error ? error.message : "Translation failed.",
          anchor: message.data.capture.anchor,
        };
      }
    });
  }
}
