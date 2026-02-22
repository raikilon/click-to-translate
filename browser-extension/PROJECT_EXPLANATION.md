# Click-to-Translate: Practical Explanation for a Developer New to Browser Extensions

This is the working mental model for this extension, based on the current code.

## 1) What this extension does

When the user performs a configured mouse + keyboard combo on a page:

1. The content script captures page context (clicked word + surrounding text/subtitle context).
2. It sends that context to the background.
3. Background runs application use cases.
4. The backend is called to translate/store.
5. The content script renders the result (tooltip or video overlay).

## 2) Extension contexts and why they exist

Browser extensions are split into contexts with different permissions/lifecycles.

1. Background service worker:
`browser-extension/infrastructure/shared/src/background/background.ts`
Purpose: central orchestration, auth/session, settings, API calls.
2. Content script:
`browser-extension/infrastructure/shared/src/content/contentScript.ts`
Purpose: listen to clicks, read DOM data, render in-page UI.
3. Popup page:
`browser-extension/infrastructure/shared/src/pages/popup.ts`
Purpose: quick actions (login/logout, inspect state).
4. Options page:
`browser-extension/infrastructure/shared/src/pages/options.ts`
Purpose: configure API/auth/trigger/languages.

Why this split is correct:

1. DOM access belongs in content script.
2. Privileged APIs and global orchestration belong in background.
3. Popup/options should be thin command UIs.

## 3) Architecture layers

The code follows onion-style layering:

1. Domain (`browser-extension/domain/src`):
Pure behavior (strategies, capture rules, display decisions, DTOs).
2. Application (`browser-extension/application/src`):
Use cases and contracts.
3. Infrastructure (`browser-extension/infrastructure/*`):
Browser APIs, storage, messaging, DOM probes, rendering, composition.

Dependency direction: `infra -> application -> domain`.

## 4) Browser-specific vs shared code

Most logic is now shared in `infrastructure/shared`.
Chrome and Firefox mostly differ by platform adapter:

1. Chrome adapter:
`browser-extension/infrastructure/chrome/src/platform/chromeAdapter.ts`
2. Firefox adapter:
`browser-extension/infrastructure/firefox/src/platform/firefoxAdapter.ts`

Both implement:
`browser-extension/infrastructure/shared/src/platform/BrowserAdapter.ts`

This is what "unified adapter with thin wrappers" means in practice.

## 5) How messages work

Requests and response contracts:
`browser-extension/infrastructure/shared/src/background/messageTypes.ts`

Request examples:

1. `GET_SETTINGS`
2. `SAVE_SETTINGS`
3. `GET_LANGUAGES`
4. `GET_POPUP_STATE`
5. `HANDLE_TRIGGER`

All responses use one envelope:

```ts
{ ok: true, data: ... }
{ ok: false, error: "...", code?: "..." }
```

`HANDLE_TRIGGER` includes extra status fields inside `data` because it represents a domain workflow result, not just CRUD.

## 6) Main classes and what they do

Domain:

1. `StrategyResolver`:
`browser-extension/domain/src/strategies/StrategyResolver.ts`
Chooses strategy by URL and optional site overrides.
2. `GenericStrategy`:
General-page extraction/display behavior.
3. `YouTubeStrategy` and `NetflixStrategy`:
Video-specific capture/display behavior.

Application:

1. `ShouldHandleTriggerUseCase`:
`browser-extension/application/src/usecases/ShouldHandleTriggerUseCase.ts`
Checks mouse/modifier combo match.
2. `HandleTranslateTriggerUseCase`:
`browser-extension/application/src/usecases/HandleTranslateTriggerUseCase.ts`
Main orchestration for capture -> bundle -> API -> render payload.
3. `EnsureAuthSessionUseCase`, `LoginUseCase`, `LogoutUseCase`:
Auth/session lifecycle.
4. `GetSelectableLanguagesUseCase`:
Fetches languages and maps selected source/target.
5. `GetSettingsUseCase`, `SaveSettingsUseCase`:
Settings read/write.

Infrastructure:

1. `HttpApiClient`:
`browser-extension/infrastructure/shared/src/impl/apiClient.ts`
Maps HTTP and backend payloads.
2. `ExtensionSettingsStore` and `ExtensionAuthSessionStore`:
Storage persistence.
3. `ExtensionAuthFlow`:
OAuth + PKCE on top of browser identity APIs.
4. `registerBackground`:
Message router and use case dispatcher.
5. `registerContentScript`:
Click handling, pre-check trigger cache, snapshot collection, rendering.

## 7) End-to-end flow from click to render

1. User clicks page.
2. Content script converts click event to domain `Trigger`.
3. Content script checks trigger match using cached settings from storage.
4. If mismatch, stop immediately.
5. If match, collect `Snapshots`:
selection + text-at-point + page info + subtitle (for supported providers).
6. Send `HANDLE_TRIGGER` to background.
7. Background checks trigger again (`ShouldHandleTriggerUseCase`) as defense-in-depth.
8. Background calls `HandleTranslateTriggerUseCase`.
9. Strategy computes capture (`word`, `sentence`, `source`, `anchor`).
10. Use case calls backend via `HttpApiClient.postSegment`.
11. Use case returns display instruction and render payload.
12. Content script renders tooltip or video overlay.

## 8) What "background" means in this project

Background is the centralized, long-lived coordinator.
It is not a UI and it does not read page DOM.
It acts as the command bus for popup/options/content.

In this codebase, background owns:

1. Routing typed messages.
2. Executing use cases.
3. Session + settings access.
4. Returning normalized success/error envelopes.

## 9) Current review findings (important)

1. Resolved: language selection now persists `language.code` from the combobox values.
This matches translate flow expectations in:
`browser-extension/application/src/usecases/HandleTranslateTriggerUseCase.ts`

2. Resolved: options now has an inline login button for language loading:
`browser-extension/infrastructure/chrome/src/pages/options/options.html`
`browser-extension/infrastructure/firefox/src/pages/options/options.html`
`browser-extension/infrastructure/shared/src/pages/options.ts`

3. Resolved: unused `PageProbe` contract removed from application exports and source.

## 10) Suggestions to simplify further

1. Rename settings fields to remove semantic confusion (`sourceLanguageId`/`targetLanguageId` currently contain codes).
2. Move identical probe wrapper configs into shared infra factories to reduce Chrome/Firefox duplication.
3. Add focused tests:
`ShouldHandleTriggerUseCase`, `HandleTranslateTriggerUseCase`, strategy capture rules, language mapping.

## 11) How to work safely in this project

When changing behavior, check layer boundaries first:

1. Domain: capture/display semantics.
2. Application: use-case orchestration and contracts.
3. Infrastructure: browser API, DOM, transport details.

If browser/DOM APIs start leaking into application/domain, architecture is drifting.
