# Click-to-Translate Extension Architecture

This document describes the architecture as implemented now.

## 1. Layering and dependencies

Dependency direction is strict:

1. `infrastructure -> application -> domain`
2. `domain` does not import `application` or `infrastructure`
3. `application` does not import `infrastructure`

Main folders:

1. `browser-extension/domain/src`
2. `browser-extension/application/src`
3. `browser-extension/infrastructure/shared/src`
4. `browser-extension/infrastructure/chrome/src`
5. `browser-extension/infrastructure/firefox/src`

## 2. Browser split

The extension now uses a shared infrastructure core with thin browser-specific adapters.

Shared core:

1. `browser-extension/infrastructure/shared/src/background/background.ts`
2. `browser-extension/infrastructure/shared/src/background/messageTypes.ts`
3. `browser-extension/infrastructure/shared/src/compositionRoot.ts`
4. `browser-extension/infrastructure/shared/src/content/contentScript.ts`
5. `browser-extension/infrastructure/shared/src/impl/*`
6. `browser-extension/infrastructure/shared/src/pages/options.ts`
7. `browser-extension/infrastructure/shared/src/pages/popup.ts`

Browser-specific wrappers:

1. `browser-extension/infrastructure/chrome/src/background/background.ts`
2. `browser-extension/infrastructure/firefox/src/background/background.ts`
3. `browser-extension/infrastructure/chrome/src/pages/*/*.ts`
4. `browser-extension/infrastructure/firefox/src/pages/*/*.ts`

Browser-specific adapters:

1. `browser-extension/infrastructure/chrome/src/platform/chromeAdapter.ts`
2. `browser-extension/infrastructure/firefox/src/platform/firefoxAdapter.ts`

These adapters implement `BrowserAdapter` (`runtime`, `storage`, `identity`, `nowMs`) from `browser-extension/infrastructure/shared/src/platform/BrowserAdapter.ts`.

## 3. Runtime contexts

The extension has four execution contexts:

1. Background service worker:
`browser-extension/infrastructure/shared/src/background/background.ts`
2. Content script:
`browser-extension/infrastructure/shared/src/content/contentScript.ts`
3. Popup page:
`browser-extension/infrastructure/shared/src/pages/popup.ts`
4. Options page:
`browser-extension/infrastructure/shared/src/pages/options.ts`

## 4. Message contracts

Requests are centralized in:
`browser-extension/infrastructure/shared/src/background/messageTypes.ts`

Request types:

1. `GET_SETTINGS`
2. `SAVE_SETTINGS`
3. `LOGIN`
4. `LOGOUT`
5. `GET_LANGUAGES`
6. `GET_POPUP_STATE`
7. `HANDLE_TRIGGER`

Envelope shape is standardized:

```ts
type MessageEnvelope<T> =
  | { ok: true; data: T }
  | { ok: false; error: string; code?: string };
```

`HANDLE_TRIGGER` still carries a domain-level status payload in `data`:
`HandleTriggerData` with `status`, optional `reason`, optional `instruction`, optional `renderPayload`.

## 5. Core application composition

Dependency wiring is in:
`browser-extension/infrastructure/shared/src/compositionRoot.ts`

Constructed services:

1. `ExtensionSettingsStore`
2. `ExtensionAuthSessionStore`
3. `SystemClock`
4. `ExtensionAuthFlow`
5. `HttpApiClient`
6. `EnsureAuthSessionUseCase`

Use cases exposed:

1. `GetSettingsUseCase`
2. `SaveSettingsUseCase`
3. `LoginUseCase`
4. `LogoutUseCase`
5. `GetSelectableLanguagesUseCase`
6. `ShouldHandleTriggerUseCase`
7. `HandleTranslateTriggerUseCase`

## 6. Click-to-translate flow

1. Content script listens to click (`registerContentScript`).
2. It maps DOM event to `Trigger`.
3. It loads/caches trigger settings from extension storage.
4. If trigger combo does not match, it exits before probe collection.
5. If trigger matches, it collects `Snapshots` and sends `HANDLE_TRIGGER`.
6. Background re-validates trigger via `ShouldHandleTriggerUseCase`.
7. Background calls `HandleTranslateTriggerUseCase`.
8. Use case resolves strategy (`generic` / `youtube` / `netflix`) from URL.
9. Strategy computes capture (`word`, `sentence`, `source`, `anchor`).
10. Use case builds `SegmentBundleDto` and calls `HttpApiClient.postSegment`.
11. Use case builds `DisplayInstruction` + `RenderPayload`.
12. Background returns envelope response.
13. Content script renders tooltip/video overlay when response is renderable.

## 7. Probe and strategy model

Generic snapshots:

1. `selection`
2. `textAtPoint`
3. `pageInfo`

Subtitle snapshot logic:

1. Provider-specific probe wrappers in:
`browser-extension/infrastructure/*/src/content/probes/youtubeProbe.ts`
`browser-extension/infrastructure/*/src/content/probes/netflixProbe.ts`
2. Shared point-based engine:
`browser-extension/infrastructure/shared/src/content/probes/pointSubtitleProbe.ts`

Domain strategies consume snapshots:

1. `GenericStrategy`
2. `YouTubeStrategy`
3. `NetflixStrategy`

`YouTubeStrategy` and `NetflixStrategy` currently require a subtitle snapshot; otherwise capture returns `null`.

## 8. Language handling

Centralized language normalization now lives in one module:
`browser-extension/application/src/model/LanguageNormalization.ts`

Used by:

1. `GetSelectableLanguagesUseCase`
2. `HandleTranslateTriggerUseCase`
3. `HttpApiClient.getLanguages`

Options language controls are backend-driven dropdowns in:

1. `browser-extension/infrastructure/chrome/src/pages/options/options.html`
2. `browser-extension/infrastructure/firefox/src/pages/options/options.html`
3. `browser-extension/infrastructure/shared/src/pages/options.ts`

## 9. Current strengths

1. Shared infrastructure core across browsers reduced duplication.
2. Trigger fast-fail in content script prevents unnecessary probe work on most clicks.
3. Popup state now comes through a background command (`GET_POPUP_STATE`), not direct popup storage access.
4. Message envelope is consistent (`ok` + `data/error`) across commands.
5. Subtitle probe logic is centralized and reused.

## 10. Remaining risks and simplification targets

1. Language identity mismatch risk:
Resolved by persisting `language.code` from options dropdown values while keeping backward matching for existing stored values.
2. Message payload heterogeneity:
Envelope is unified, but `HANDLE_TRIGGER` adds `status/reason` payload semantics not used elsewhere.
3. Probe wrappers duplication:
Chrome and Firefox subtitle probe wrappers are identical and can be moved to shared infra.
4. Missing focused tests:
No behavior tests around trigger handling, strategy capture rules, and language selection mapping.
