import type {
  AuthSession,
  GetSelectableLanguagesResult,
  HandleTranslateTriggerResult,
  RenderPayload,
  Settings,
} from "@application";
import type { DisplayInstruction, Snapshots, Trigger } from "@domain";

export interface GetSettingsMessage {
  type: "GET_SETTINGS";
}

export interface SaveSettingsMessage {
  type: "SAVE_SETTINGS";
  settings: Settings;
}

export interface LoginMessage {
  type: "LOGIN";
}

export interface LogoutMessage {
  type: "LOGOUT";
}

export interface GetLanguagesMessage {
  type: "GET_LANGUAGES";
}

export interface HandleTriggerMessage {
  type: "HANDLE_TRIGGER";
  trigger: Trigger;
  snapshots: Snapshots;
}

export type ExtensionRequestMessage =
  | GetSettingsMessage
  | SaveSettingsMessage
  | LoginMessage
  | LogoutMessage
  | GetLanguagesMessage
  | HandleTriggerMessage;

export interface ErrorMessageResponse {
  ok: false;
  error: string;
}

export interface GetSettingsResponse {
  ok: true;
  settings: Settings;
}

export interface SaveSettingsResponse {
  ok: true;
  settings: Settings;
}

export interface LoginResponse {
  ok: true;
  session: AuthSession | null;
}

export interface LogoutResponse {
  ok: true;
}

export interface GetLanguagesResponse {
  ok: true;
  result: GetSelectableLanguagesResult;
}

export type HandleTriggerMessageStatus =
  | "ok"
  | Exclude<HandleTranslateTriggerResult["status"], "rendered">;

export interface HandleTriggerResponse {
  status: HandleTriggerMessageStatus;
  reason?: string;
  instruction?: DisplayInstruction;
  renderPayload?: RenderPayload;
  triggerResult?: HandleTranslateTriggerResult;
  error?: string;
}

export type ExtensionResponseMessage =
  | ErrorMessageResponse
  | GetSettingsResponse
  | SaveSettingsResponse
  | LoginResponse
  | LogoutResponse
  | GetLanguagesResponse
  | HandleTriggerResponse;

export function isHandleTriggerMessage(
  message: ExtensionRequestMessage,
): message is HandleTriggerMessage {
  return message.type === "HANDLE_TRIGGER";
}
