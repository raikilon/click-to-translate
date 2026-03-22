import { onBackgroundMessage, SERVICES } from "@/entrypoints/shared/messaging/services";
import type { TranslateRequest } from "@/entrypoints/shared/messaging/contracts/TranslateRequest";
import { LoginUseCase } from "@/content/authentication/application/LoginUseCase";
import {
  AuthRequiredError,
  AuthSessionExpiredError,
  GetAuthStateUseCase,
  LogoutUseCase,
} from '@vocabulary/auth';
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
  private loginInFlight: Promise<void> | null = null;

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
      try {
        return await this.dependencies.listTranslationLanguagesUseCase.execute();
      } catch (error) {
        if (this.isAuthError(error)) {
          void this.triggerLoginFlow();
          return [];
        }

        throw error;
      }
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
        if (this.isAuthError(error)) {
          void this.triggerLoginFlow();
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

  private isAuthError(error: unknown): boolean {
    return error instanceof AuthRequiredError || error instanceof AuthSessionExpiredError;
  }

  private triggerLoginFlow(): Promise<void> {
    if (this.loginInFlight) {
      return this.loginInFlight;
    }

    this.loginInFlight = this.dependencies.loginUseCase
      .execute()
      .then(() => undefined)
      .catch(() => undefined)
      .finally(() => {
        this.loginInFlight = null;
      });

    return this.loginInFlight;
  }
}
