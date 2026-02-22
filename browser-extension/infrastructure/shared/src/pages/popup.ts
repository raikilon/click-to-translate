import type {
  ErrorMessageResponse,
  GetLanguagesResponse,
  GetSettingsResponse,
  LoginResponse,
} from "../background/messageTypes";
import type { RuntimePort, StoragePort } from "../platform/BrowserAdapter";

interface PopupDependencies {
  runtime: RuntimePort;
  storage: StoragePort;
}

function byId<T extends HTMLElement>(id: string): T {
  const element = document.getElementById(id);
  if (!element) {
    throw new Error(`Missing popup element: ${id}`);
  }

  return element as T;
}

function setStatus(message: string, isError = false): void {
  const status = byId<HTMLDivElement>("status");
  status.textContent = message;
  status.dataset.kind = isError ? "error" : "ok";
}

function isErrorResponse<TResponse>(
  response: TResponse | ErrorMessageResponse,
): response is ErrorMessageResponse {
  return (
    response &&
    typeof response === "object" &&
    "ok" in response &&
    (response as ErrorMessageResponse).ok === false
  );
}

function createMessageSender(runtime: RuntimePort) {
  return async function sendMessage<TResponse>(message: unknown): Promise<TResponse> {
    const response = await runtime.sendMessage<TResponse | ErrorMessageResponse>(message);
    if (!response) {
      throw new Error("No response from background.");
    }

    if (isErrorResponse(response)) {
      throw new Error(response.error);
    }

    return response as TResponse;
  };
}

export function registerPopup(dependencies: PopupDependencies): void {
  const sendMessage = createMessageSender(dependencies.runtime);

  async function refreshPopupState(): Promise<void> {
    const [storedSession, settingsResponse] = await Promise.all([
      dependencies.storage.get<unknown>("authSession"),
      sendMessage<GetSettingsResponse>({ type: "GET_SETTINGS" }),
    ]);

    const sessionLabel = byId<HTMLDivElement>("sessionState");
    const languageLabel = byId<HTMLDivElement>("languageState");

    sessionLabel.textContent = storedSession
      ? "Session: Logged in"
      : "Session: Logged out";

    const source = settingsResponse.settings.sourceLanguageId ?? "?";
    const target = settingsResponse.settings.targetLanguageId ?? "?";
    languageLabel.textContent = `Languages: ${source} -> ${target}`;
  }

  async function onLogin(): Promise<void> {
    const response = await sendMessage<LoginResponse>({ type: "LOGIN" });
    if (response.session) {
      setStatus("Login successful.");
    } else {
      setStatus("Login did not return an active session.", true);
    }

    await refreshPopupState();
  }

  async function onLogout(): Promise<void> {
    await sendMessage({ type: "LOGOUT" });
    setStatus("Logged out.");
    await refreshPopupState();
  }

  async function onGetLanguages(): Promise<void> {
    const response = await sendMessage<GetLanguagesResponse>({
      type: "GET_LANGUAGES",
    });
    const names = response.result.languages.map((language) => language.code).join(", ");

    byId<HTMLPreElement>("languagesOutput").textContent =
      names || "No languages returned.";
    setStatus("Languages loaded.");
  }

  byId<HTMLButtonElement>("loginButton").addEventListener("click", () => {
    void onLogin().catch((error: unknown) => {
      setStatus(error instanceof Error ? error.message : "Login failed.", true);
    });
  });

  byId<HTMLButtonElement>("logoutButton").addEventListener("click", () => {
    void onLogout().catch((error: unknown) => {
      setStatus(error instanceof Error ? error.message : "Logout failed.", true);
    });
  });

  byId<HTMLButtonElement>("languagesButton").addEventListener("click", () => {
    void onGetLanguages().catch((error: unknown) => {
      setStatus(error instanceof Error ? error.message : "Language request failed.", true);
    });
  });

  void refreshPopupState().catch((error: unknown) => {
    setStatus(error instanceof Error ? error.message : "Failed to load popup state.", true);
  });
}
