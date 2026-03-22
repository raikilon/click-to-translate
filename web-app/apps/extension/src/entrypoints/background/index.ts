import { LoginUseCase } from "@/content/authentication/application/LoginUseCase";
import {
  GatewayAuthService,
  GetAuthStateUseCase,
  LogoutUseCase,
} from '@vocabulary/auth';
import { authRuntimeConfig } from "@/content/authentication/infrastructure/AuthRuntimeConfig";
import { LoginTabAuthFlow } from "@/content/authentication/infrastructure/LoginTabAuthFlow";
import { GetLanguagePrefsUseCase } from "@/content/translation/application/GetLanguagePrefsUseCase";
import { ListTranslationLanguagesUseCase } from "@/content/translation/application/ListTranslationLanguagesUseCase";
import { TranslateSelectionUseCase } from "@/content/translation/application/TranslateSelectionUseCase";
import { LanguagePrefsRepository } from "@/content/translation/infrastructure/LanguagePrefsRepository";
import { TranslationApi } from "@/content/translation/infrastructure/TranslationApi";
import { TranslationGateway } from "@/content/translation/infrastructure/TranslationGateway";
import { BackgroundMessageRouter } from "./BackgroundMessageRouter";

export default defineBackground(() => {
  const gatewayAuthService = new GatewayAuthService({
    apiBaseUrl: authRuntimeConfig.apiBaseUrl,
  });
  const loginTabAuthFlow = new LoginTabAuthFlow();
  const getAuthStateUseCase = new GetAuthStateUseCase(gatewayAuthService);
  const loginUseCase = new LoginUseCase(gatewayAuthService, loginTabAuthFlow);
  const logoutUseCase = new LogoutUseCase(gatewayAuthService);

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





