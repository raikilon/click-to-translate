import { defineExtensionMessaging } from "@webext-core/messaging";
import type {
  AuthState,
  TranslateClickResult,
} from "@application";
import type { CapturedClick } from "@domain";

export const SERVICES = {
  translateAtPoint: "translateAtPoint",
  getTranslationLanguages: "getTranslationLanguages",
  login: "login",
  logout: "logout",
  getAuthState: "getAuthState",
} as const;

export interface BackgroundProtocol {
  [SERVICES.translateAtPoint]: (payload: {
    capture: CapturedClick;
  }) => TranslateClickResult;
  [SERVICES.getTranslationLanguages]: (_: Record<string, never>) => string[];
  [SERVICES.login]: (_: Record<string, never>) => AuthState;
  [SERVICES.logout]: (_: Record<string, never>) => Record<string, never>;
  [SERVICES.getAuthState]: (_: Record<string, never>) => AuthState;
}

export type BackgroundService = keyof BackgroundProtocol;
export type BackgroundPayload<TService extends BackgroundService> =
  Parameters<BackgroundProtocol[TService]>[0];
export type BackgroundResponse<TService extends BackgroundService> =
  Awaited<ReturnType<BackgroundProtocol[TService]>>;

const messaging = defineExtensionMessaging<BackgroundProtocol>();

export const sendBackgroundMessage = messaging.sendMessage;
export const onBackgroundMessage = messaging.onMessage;
