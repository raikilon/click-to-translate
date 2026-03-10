import { GetAuthStateUseCase } from "@/content/authentication/application/GetAuthStateUseCase";
import { LoginUseCase } from "@/content/authentication/application/LoginUseCase";
import { LogoutUseCase } from "@/content/authentication/application/LogoutUseCase";
import { AuthSessionManager } from "@/content/authentication/infrastructure/AuthSessionManager";
import { GatewayAuthClient } from "@/content/authentication/infrastructure/GatewayAuthClient";
import { LoginTabAuthFlow } from "@/content/authentication/infrastructure/LoginTabAuthFlow";
import { GetLanguagePrefsUseCase } from "@/content/translation/application/GetLanguagePrefsUseCase";
import { ListTranslationLanguagesUseCase } from "@/content/translation/application/ListTranslationLanguagesUseCase";
import { TranslateSelectionUseCase } from "@/content/translation/application/TranslateSelectionUseCase";
import { LanguagePrefsRepository } from "@/content/translation/infrastructure/LanguagePrefsRepository";
import { TranslationApi } from "@/content/translation/infrastructure/TranslationApi";
import { TranslationGateway } from "@/content/translation/infrastructure/TranslationGateway";
import { BackgroundMessageRouter } from "./BackgroundMessageRouter";

export default defineBackground(() => {
  const gatewayAuthClient = new GatewayAuthClient();
  const loginTabAuthFlow = new LoginTabAuthFlow();
  const authSessionManager = new AuthSessionManager({
    gatewayAuthClient,
    loginTabAuthFlow,
  });
  const getAuthStateUseCase = new GetAuthStateUseCase(authSessionManager);
  const loginUseCase = new LoginUseCase(authSessionManager);
  const logoutUseCase = new LogoutUseCase(authSessionManager);

  const translationApi = new TranslationApi();
  const translationGateway = new TranslationGateway(translationApi);
  const languagePrefsRepository = new LanguagePrefsRepository();
  const getLanguagePrefsUseCase = new GetLanguagePrefsUseCase(languagePrefsRepository);
  const listTranslationLanguagesUseCase = new ListTranslationLanguagesUseCase(translationApi);
  const translateSelectionUseCase = new TranslateSelectionUseCase(translationGateway);

  new BackgroundMessageRouter({
    loginUseCase,
    logoutUseCase,
    getAuthStateUseCase,
    listTranslationLanguagesUseCase,
    getLanguagePrefsUseCase,
    translateSelectionUseCase,
  }).register();
});





