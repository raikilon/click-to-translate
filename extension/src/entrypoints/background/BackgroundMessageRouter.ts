import { onBackgroundMessage, SERVICES } from "@/entrypoints/shared/messaging/services";
import type { TranslateRequest } from "@/entrypoints/shared/messaging/contracts/TranslateRequest";
import { AuthRequiredError, AuthSessionExpiredError } from "@/content/authentication/application/AuthErrors";
import { GetAuthStateUseCase } from "@/content/authentication/application/GetAuthStateUseCase";
import { LoginUseCase } from "@/content/authentication/application/LoginUseCase";
import { LogoutUseCase } from "@/content/authentication/application/LogoutUseCase";
import { GetLanguagePrefsUseCase } from "@/content/translation/application/GetLanguagePrefsUseCase";
import { ListTranslationLanguagesUseCase } from "@/content/translation/application/ListTranslationLanguagesUseCase";
import { TranslateSelectionUseCase } from "@/content/translation/application/TranslateSelectionUseCase";
import { TranslateSelectionInput } from "@/content/translation/domain/TranslateSelectionInput";

interface BackgroundMessageRouterDependencies {
  loginUseCase: LoginUseCase;
  logoutUseCase: LogoutUseCase;
  getAuthStateUseCase: GetAuthStateUseCase;
  listTranslationLanguagesUseCase: ListTranslationLanguagesUseCase;
  getLanguagePrefsUseCase: GetLanguagePrefsUseCase;
  translateSelectionUseCase: TranslateSelectionUseCase;
}

export class BackgroundMessageRouter {
  constructor(private readonly dependencies: BackgroundMessageRouterDependencies) {}

  register(): void {
    onBackgroundMessage(SERVICES.login, async () => {
      return this.dependencies.loginUseCase.execute();
    });

    onBackgroundMessage(SERVICES.logout, async () => {
      await this.dependencies.logoutUseCase.execute();
      return {};
    });

    onBackgroundMessage(SERVICES.getAuthState, async () => {
      return this.dependencies.getAuthStateUseCase.execute();
    });

    onBackgroundMessage(SERVICES.getTranslationLanguages, async () => {
      return this.dependencies.listTranslationLanguagesUseCase.execute();
    });

    onBackgroundMessage(SERVICES.translateWord, async (message) => {
      const request = message.data.request as TranslateRequest;
      if (request.kind !== "translateWord") {
        return {
          kind: "error" as const,
          message: "Unsupported request kind.",
        };
      }

      const prefs = await this.dependencies.getLanguagePrefsUseCase.execute();
      if (!prefs.sourceLanguageId || !prefs.targetLanguageId) {
        return {
          kind: "missingLanguagePrefs" as const,
        };
      }

      try {
        const translatedText = await this.dependencies.translateSelectionUseCase.execute(
          new TranslateSelectionInput(
            request.word,
            request.context,
            prefs.sourceLanguageId,
            prefs.targetLanguageId,
          ),
        );

        if (!translatedText) {
          return {
            kind: "error" as const,
            message: "No translation available.",
          };
        }

        return {
          kind: "ok" as const,
          translation: translatedText,
        };
      } catch (error) {
        if (error instanceof AuthRequiredError || error instanceof AuthSessionExpiredError) {
          return {
            kind: "unauthenticated" as const,
          };
        }

        return {
          kind: "error" as const,
          message: error instanceof Error ? error.message : "Translation failed.",
        };
      }
    });
  }
}
