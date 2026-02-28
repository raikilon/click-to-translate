import {
  type LanguagePrefsStore,
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
import { AuthRequiredError } from "./auth/AuthErrors";
import { TranslationGateway } from "./TranslationGateway";
import { languagePrefsStorageItem } from "./storage/items";

export class Background {
  register(): void {
    const authSessionManager = new AuthSessionManager();
    const languagePrefsStore: LanguagePrefsStore = {
      get: () => languagePrefsStorageItem.getValue(),
      set: (prefs) => languagePrefsStorageItem.setValue(prefs),
    };
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

    onBackgroundMessage(SERVICES.getTranslationLanguages, async () => {
      const accessToken = await authSessionManager.getAccessToken(false);
      if (!accessToken) {
        throw new AuthRequiredError();
      }

      return translationApi.listLanguages(accessToken);
    });

    onBackgroundMessage(SERVICES.translateAtPoint, async (message) => {
      let capturedClick: CapturedClick;
      try {
        capturedClick = CapturedClick.hydrate(message.data.capture);
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

      return translateClick.execute(capturedClick);
    });
  }
}
