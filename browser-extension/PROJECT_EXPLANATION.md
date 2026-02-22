# Click-to-Translate: Project Explanation for a Developer New to Browser Extensions

This file explains the browser extension part of the project in practical terms so you can start working on it confidently.

## 1) What a browser extension is in this project

This extension listens for a click in the web page, captures text context near that click, sends it to the backend for translation/saving, and shows the result as a tooltip or video overlay.

It runs in multiple extension contexts:

1. `background` service worker:
`infrastructure/chrome/src/background/background.ts` (and Firefox equivalent)
This is the central coordinator. It handles messages and runs use cases.
2. `content script`:
`infrastructure/chrome/src/content/contentScript.ts`
This runs inside each web page, can read DOM and click events, and can render UI in the page.
3. `popup page`:
`infrastructure/chrome/src/pages/popup/*`
Small UI to login/logout and inspect languages.
4. `options page`:
`infrastructure/chrome/src/pages/options/*`
UI for saving extension settings (API URL, auth config, trigger keys, etc).

## 2) Project architecture: why it is split this way

The extension is intentionally layered:

1. `domain/`
Pure models + strategy logic. No browser APIs and no DOM types.
2. `application/`
Use cases and contracts (interfaces). Orchestrates business flow.
3. `infrastructure/`
Actual browser-specific code (`chrome.*` / `browser.*`), storage, messaging, DOM probes, renderers.

Flow direction is one-way: `Infrastructure -> Application -> Domain`.

Why this exists:

1. Keep business logic testable and stable.
2. Keep browser differences (Chrome vs Firefox) mostly at the edges.
3. Avoid leaking `chrome.*` or DOM objects into core logic.

## 3) Main classes/modules and what each does

## Domain layer (`browser-extension/domain/src`)

1. Canonical models:
`model/SegmentBundle.ts`, `model/Language.ts`, `model/CaptureResult.ts`, `model/DisplayInstruction.ts`, etc.
These define the "language" shared by the project.
2. Strategy interface:
`strategies/Strategy.ts`
A strategy knows how to capture text and how to display result for a page type.
3. Strategy resolver:
`strategies/StrategyResolver.ts`
Picks a strategy from URL and optional overrides.
4. Concrete strategies:
`GenericStrategy`, `YouTubeStrategy`, `NetflixStrategy`
They define behavior for normal pages vs video pages.

## Application layer (`browser-extension/application/src`)

1. `HandleTranslateTriggerUseCase`
Core use case for click translation. It checks trigger settings, resolves strategy, builds DTO, posts to API, prepares display instruction.
2. `EnsureAuthSessionUseCase`
Session lifecycle: reuse token, refresh token, optionally interactive login.
3. `GetSelectableLanguagesUseCase`
Loads available languages and maps current selected source/target.
4. `GetSettingsUseCase` / `SaveSettingsUseCase`
Read/write normalized settings.
5. `LoginUseCase` / `LogoutUseCase`
Auth actions for popup.
6. Contracts:
`ApiClient`, `AuthFlow`, `AuthSessionStore`, `SettingsStore`, `Renderer`, `Clock`, `PageProbe`.
These are interfaces implemented by infrastructure.

## Infrastructure layer (`browser-extension/infrastructure/*/src`)

1. `compositionRoot.ts`
Dependency wiring. Creates stores, auth flow, api client, use cases.
2. `background/background.ts`
Message router. Receives commands from popup/options/content and executes the right use case.
3. `background/messageTypes.ts`
Message and response contracts between contexts.
4. `content/contentScript.ts`
Converts click to `Trigger`, collects snapshots, sends `HANDLE_TRIGGER`, then renders response.
5. Probes (`content/probes/*`)
Gather snapshots:
selection text, text-at-point, subtitle snapshot (YouTube/Netflix), page info.
6. Renderers (`content/render/*`)
Render tooltip or video overlay inside page.
7. Impl adapters (`impl/*`)
`apiClient.ts`, `authFlow.ts`, `settingsStore.ts`, `authSessionStore.ts`, `clock.ts`, `pkce.ts`.
These bind extension APIs to application contracts.

## 4) How messaging works in this project

Messaging is the communication channel between extension contexts.

Key message types:

1. `GET_SETTINGS`
2. `SAVE_SETTINGS`
3. `LOGIN`
4. `LOGOUT`
5. `GET_LANGUAGES`
6. `HANDLE_TRIGGER`

Where defined:
`infrastructure/chrome/src/background/messageTypes.ts` and Firefox equivalent.

Why this is done:

1. Content script cannot own all privileged logic.
2. Popup/options need shared centralized state/actions.
3. Background provides one orchestration point and consistent behavior.

Chrome specific detail:

Background listener uses callback style and returns `true` to keep async response channel open.

Firefox specific detail:

Background listener returns a Promise directly.

## 5) End-to-end flow from classes (no diagram)

When user clicks on page:

1. `contentScript.ts` listens to click, builds `Trigger`.
2. `probeResolver.ts` collects `Snapshots`:
page info + selection + text at click + optional subtitles.
3. Content sends `HANDLE_TRIGGER` to background.
4. `background.ts` routes message to `HandleTranslateTriggerUseCase`.
5. Use case loads settings and validates trigger vs configured mouse/modifier combo.
6. Use case resolves strategy with `StrategyResolver`.
7. Strategy computes `CaptureResult` (word/sentence/source/anchor).
8. Use case resolves source/target language from settings.
9. Use case optionally resolves auth session (`EnsureAuthSessionUseCase`).
10. Use case sends segment bundle via `HttpApiClient.postSegment(...)`.
11. Use case builds `DisplayInstruction` and `RenderPayload`.
12. Background returns response to content.
13. Content calls `ContentRendererBridge.render(...)`.
14. Renderer shows tooltip or video overlay.

## 6) Why background/content split matters

1. Content script is close to the page DOM, so it should capture page data and render.
2. Background is centralized and survives independent of page lifecycle, so it should orchestrate auth/settings/API actions.
3. Popup/options are UI shells that send commands, not business logic owners.

This separation is standard extension architecture and is the right direction.

## 7) What is wrong or not ideal today (standards + project objective)

Objective is simple ("click, translate, save"), but implementation has avoidable complexity.

1. Chrome/Firefox duplication is high
Most files are copied with tiny API differences.
Impact: double maintenance, more bug risk.
2. Messaging protocol is inconsistent
Most responses use `{ ok: true/false }`, but trigger flow uses `{ status: ... }`.
Impact: unnecessary branching and mental overhead.
3. Popup reads auth session directly from storage
`popup.ts` checks storage directly instead of using a single app contract path.
Impact: split source of truth.
4. Expensive probe work happens for every click before trigger filter
Click is always captured and snapshot collection runs before quickly rejecting wrong key combination in background.
Impact: avoidable overhead.
5. Subtitle probes are very heuristic-heavy
Large DOM scanning and scoring logic, especially Netflix probe.
Impact: hard to maintain and performance-sensitive.
6. Permissions are broad
`<all_urls>` and broad host permissions are useful for MVP, but too wide for production hardening.
7. Language normalization logic is duplicated
Some normalization in application (`LanguageUtils`) and again in infra API client.
Impact: drift risk.
8. No behavior tests around core flow
Typecheck passes, but there are no targeted tests for use-case/strategy behavior.

## 8) Where overcomplication likely happened

1. Over-optimizing architecture early for dual-browser support by copying entire infrastructure trees.
2. Building very strong subtitle heuristics before a minimal reliable baseline existed.
3. Mixing "transport response shape" styles (`ok` and `status`) during iterations.
4. Allowing popup shortcuts (direct storage reads) for speed.

## 9) Practical simplification plan

1. Unify browser adapter
Create thin wrappers for `runtime`, `storage`, `identity`, then share one infra implementation for both browsers.
2. Standardize message envelope
Use one format for all responses, for example:
`{ ok: boolean, data?: T, error?: string, code?: string }`.
3. Fast fail in content script before probes
Load trigger settings once (or cache) and skip heavy snapshot collection when combo does not match.
4. Move popup session state to background command
No direct storage reads from popup.
5. Reduce probe scope
Keep robust selectors, but reduce broad fallback scans and add guardrails/time limits.
6. Centralize language normalization
One module only.
7. Add small targeted tests
At least:
`HandleTranslateTriggerUseCase`,
`StrategyResolver`,
`GenericStrategy/YouTubeStrategy/NetflixStrategy`.

## 10) Mental model you can use while developing

If you change behavior:

1. Domain: "How do we decide capture/display semantics?"
2. Application: "How do we orchestrate settings/auth/api for this action?"
3. Infrastructure: "How do we read browser/DOM APIs and map to domain types?"

If a change leaks browser API types into application/domain, it is usually the wrong layer.

---

If you want, next step can be a "developer onboarding checklist" file with the exact places to start for:
1. changing click behavior,
2. adding a new site strategy,
3. changing auth,
4. changing backend payload mapping.
