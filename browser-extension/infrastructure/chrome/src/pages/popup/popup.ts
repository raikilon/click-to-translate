import type {
  ErrorMessageResponse,
  GetLanguagesResponse,
  GetSettingsResponse,
  LoginResponse,
} from "../../background/messageTypes";

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

function sendMessage<TResponse>(message: unknown): Promise<TResponse> {
  return new Promise((resolve, reject) => {
    chrome.runtime.sendMessage(message, (response?: TResponse | ErrorMessageResponse) => {
      if (chrome.runtime.lastError) {
        reject(new Error(chrome.runtime.lastError.message));
        return;
      }

      if (!response) {
        reject(new Error("No response from background."));
        return;
      }

      if (
        response &&
        typeof response === "object" &&
        "ok" in response &&
        (response as ErrorMessageResponse).ok === false
      ) {
        reject(new Error((response as ErrorMessageResponse).error));
        return;
      }

      resolve(response as TResponse);
    });
  });
}

function readStoredSession(): Promise<unknown> {
  return new Promise((resolve) => {
    chrome.storage.local.get(["authSession"], (items) => {
      resolve(items.authSession);
    });
  });
}

async function refreshPopupState(): Promise<void> {
  const [storedSession, settingsResponse] = await Promise.all([
    readStoredSession(),
    sendMessage<GetSettingsResponse>({ type: "GET_SETTINGS" }),
  ]);

  const sessionLabel = byId<HTMLDivElement>("sessionState");
  const languageLabel = byId<HTMLDivElement>("languageState");

  sessionLabel.textContent = storedSession ? "Session: Logged in" : "Session: Logged out";

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
  const response = await sendMessage<GetLanguagesResponse>({ type: "GET_LANGUAGES" });
  const names = response.result.languages.map((language) => language.code).join(", ");

  byId<HTMLPreElement>("languagesOutput").textContent = names || "No languages returned.";
  setStatus("Languages loaded.");
}

function boot(): void {
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

boot();
