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

export interface GetPopupStateMessage {
  type: "GET_POPUP_STATE";
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
  | GetPopupStateMessage
  | HandleTriggerMessage;

export interface MessageSuccess<TData> {
  ok: true;
  data: TData;
}

export interface MessageError {
  ok: false;
  error: string;
  code?: string;
}

export type MessageEnvelope<TData> = MessageSuccess<TData> | MessageError;

export interface GetSettingsData {
  settings: Settings;
}

export interface SaveSettingsData {
  settings: Settings;
}

export interface LoginData {
  session: AuthSession | null;
}

export type LogoutData = Record<string, never>;

export interface GetLanguagesData {
  result: GetSelectableLanguagesResult;
}

export interface GetPopupStateData {
  loggedIn: boolean;
  sourceLanguageId: string | null;
  targetLanguageId: string | null;
}

export type HandleTriggerStatus = HandleTranslateTriggerResult["status"];

export interface HandleTriggerData {
  status: HandleTriggerStatus;
  reason?: string;
  instruction?: DisplayInstruction;
  renderPayload?: RenderPayload;
  triggerResult: HandleTranslateTriggerResult;
}

export type GetSettingsResponse = MessageEnvelope<GetSettingsData>;
export type SaveSettingsResponse = MessageEnvelope<SaveSettingsData>;
export type LoginResponse = MessageEnvelope<LoginData>;
export type LogoutResponse = MessageEnvelope<LogoutData>;
export type GetLanguagesResponse = MessageEnvelope<GetLanguagesData>;
export type GetPopupStateResponse = MessageEnvelope<GetPopupStateData>;
export type HandleTriggerResponse = MessageEnvelope<HandleTriggerData>;

export type ExtensionResponseMessage =
  | GetSettingsResponse
  | SaveSettingsResponse
  | LoginResponse
  | LogoutResponse
  | GetLanguagesResponse
  | GetPopupStateResponse
  | HandleTriggerResponse;
