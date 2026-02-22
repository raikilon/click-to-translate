## General concept and rules (for Step 5)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. Infrastructure is responsible for:

   * using extension APIs (`chrome.*`)
   * capturing raw browser events
   * reading DOM and converting it into **canonical domain objects** (`Trigger`, `SelectionSnapshot`, `TextAtPoint`, `SubtitleSnapshot`, `PageInfo`)
   * dependency injection (composition root)
3. Infrastructure must **not** contain orchestration logic; it only:

   * maps inputs
   * calls the appropriate Application use case(s)
   * renders according to the `DisplayInstruction` via the `Renderer` contract implementation

### Implementation rules

* Implement a minimal Chrome target that runs end-to-end on generic pages:

  * options page can save settings
  * popup can login/logout
  * content script can send `Trigger + snapshots` to background
  * background runs `HandleTrigger` use case
  * content script renders result using a renderer
* Keep everything browser-specific inside `infrastructure/chrome`.
* Any DOM API inconsistencies (caret APIs) are solved inside probes by feature detection and mapped into canonical objects.

---

## Step 5: Chrome infrastructure MVP (background + messaging + storage/auth/http + content probe+renderer)

### Objective

Make the first real working build for **Chrome**:

* background routes messages to use cases (DI)
* implements Application contracts:

  * SettingsStore, AuthSessionStore, AuthFlow, ApiClient, Clock
* content script implements:

  * mapping click → `Trigger`
  * probes to produce snapshots
  * renderer to display `DisplayInstruction` (tooltip only for now)
* minimal Options + Popup wiring (no polish)

---

## Files to create (exact paths)

Under `browser-extension/infrastructure/chrome/`:

### A) Packaging

1. `manifest.json`

### B) Composition + background

2. `src/compositionRoot.ts`
3. `src/background/background.ts`
4. `src/background/messageTypes.ts`

### C) Contract implementations (background-side)

5. `src/impl/settingsStore.ts`
6. `src/impl/authSessionStore.ts`
7. `src/impl/clock.ts`
8. `src/impl/apiClient.ts`
9. `src/impl/authFlow.ts`
10. `src/impl/pkce.ts` (helper)

### D) Content script (DOM mapping + rendering)

11. `src/content/contentScript.ts`
12. `src/content/probes/genericPageProbe.ts`
13. `src/content/probes/textAtPointProbe.ts` (caret feature detection)
14. `src/content/render/tooltipRenderer.ts`
15. `src/content/render/rendererBridge.ts` (implements Application `Renderer` contract *in content context*)

### E) Extension pages (minimal)

16. `src/pages/options/options.ts` (+ minimal HTML/CSS as needed)
17. `src/pages/popup/popup.ts` (+ minimal HTML/CSS as needed)

(If you already have a UI stack, integrate accordingly; the key is message calls.)

---

## Exact responsibilities and contents

### 1) `manifest.json` (MV3)

Must include:

* background service worker: `src/background/background.ts` bundle output
* content script matches (initially `<all_urls>` or your preferred scope):

  * inject `contentScript` at `document_idle`
* permissions:

  * `storage`
  * `identity` (needed for OAuth web auth flow)
* host_permissions:

  * your API origin(s) and auth origin(s)
* action default popup (your popup html)
* options_page (your options html)

**Acceptance**

* Chrome loads the unpacked extension without errors.

---

## Messaging design (Chrome MVP)

### 4) `src/background/messageTypes.ts`

Define message DTOs (keep simple, no shared folder):

* From Options/Popup:

  * `GET_SETTINGS`
  * `SAVE_SETTINGS { settings }`
  * `LOGIN`
  * `LOGOUT`
  * `GET_LANGUAGES`

* From Content:

  * `HANDLE_TRIGGER { trigger, snapshots }`

* From Background → Content:

  * Background response includes:

    * status union from `HandleTrigger`
    * if status OK, include `instruction` and `renderPayload` (translation text)

**Rule**

* Content script sends canonical objects only (Trigger/Snapshots).
* Background never receives DOM objects.

---

## Background wiring (DI + routing)

### 2) `src/compositionRoot.ts`

Creates instances of Application use cases with injected Chrome implementations.

It must:

* instantiate contract impls:

  * `SettingsStore` → `settingsStore.ts`
  * `AuthSessionStore` → `authSessionStore.ts`
  * `AuthFlow` → `authFlow.ts`
  * `ApiClient` → `apiClient.ts`
  * `Clock` → `clock.ts`
* return factories or concrete use case instances:

  * `HandleTrigger`, `Login`, `Logout`, `LoadLanguages`, `SaveSettings`

**Important**

* `Renderer` is not implemented in background (DOM rendering happens in content).
* In Step 5 MVP, the background `HandleTrigger` call should **not** call `Renderer.render` internally.

  * Instead: modify your wiring so `HandleTrigger` use case returns `instruction + payload` and the content script renders it.
  * If your Step 4 `HandleTrigger` currently calls `Renderer`, inject a “NoopRenderer” in background and also return the instruction/payload so content can render. (Prefer returning and rendering in content.)

### 3) `src/background/background.ts`

* `chrome.runtime.onMessage.addListener((msg, sender) => ...)`
* switch on `msg.type`
* call relevant use case and `sendResponse(result)`
* For `HANDLE_TRIGGER`, ensure `sender.tab?.id` exists if you later need to push messages; for MVP you can reply directly.

**Acceptance**

* Popup/options can call GET_SETTINGS/SAVE_SETTINGS and receive responses.

---

## Contract implementations (background)

### 5) `settingsStore.ts`

* uses `chrome.storage.local`
* key: `settings`
* `get()` returns stored or `DEFAULT_SETTINGS` from Application model
* `save(settings)` persists

### 6) `authSessionStore.ts`

* uses `chrome.storage.local`
* key: `authSession`
* `get/set/clear`

### 7) `clock.ts`

* `nowMs()` uses `Date.now()`

### 8) `apiClient.ts`

Implements:

* `getLanguages(accessTokenOrNull)`
* `postSegment(accessToken, bundle)`
* `refreshAccessToken(refreshToken)`

Implementation notes:

* Base URLs and paths come from settings (the use case can pass full URL, or ApiClient reads settings; choose one and keep it consistent).
* Add `Authorization: Bearer ...` only when access token provided.
* Parse JSON responses; throw typed errors.

### 9) `authFlow.ts` + 10) `pkce.ts`

Implements OAuth2 Authorization Code + PKCE using `chrome.identity.launchWebAuthFlow`.

`pkce.ts` must provide:

* `generateCodeVerifier()`
* `codeChallengeS256(verifier)` (SHA-256 + base64url)
* `generateState()`

`authFlow.ts` must:

* read settings for `authorizeUrl`, `tokenUrl`, `clientId`, scopes
* build auth URL with PKCE + state
* call launchWebAuthFlow
* parse redirect URL to extract `code` and `state`
* exchange code for tokens via token endpoint
* return `AuthSession { accessToken, refreshToken?, expiresAtMs }`

**Acceptance**

* Clicking “Login” in popup yields stored session.

---

## Content script (DOM mapping + probes + rendering)

### 11) `contentScript.ts`

Responsibilities:

* On click event:

  * construct canonical `Trigger` (url, coords, button, modifiers, selectedText?, occurredAtMs)
  * call `genericPageProbe` to produce snapshots:

    * selection snapshot
    * text-at-point snapshot
    * page info
    * subtitle snapshot = null (for MVP)
  * send message to background: `HANDLE_TRIGGER { trigger, snapshots }`
  * if response status OK:

    * call tooltip renderer with `instruction + payload`

**Rule**

* Do not check hotkey in content if Step 4 checks it in use case (avoid duplication). The use case will return `IGNORED` if it doesn’t match.

### 12) `genericPageProbe.ts`

Produces:

* `PageInfo` (title, url)
* `SelectionSnapshot`:

  * `window.getSelection()?.toString().trim()` if non-empty
  * anchor as point at click (rect anchor optional later)
* `TextAtPoint` via `textAtPointProbe.getTextAtPoint(trigger)`

Returns a `snapshots` object shaped like the domain strategies expect.

### 13) `textAtPointProbe.ts`

Implements browser-variant caret logic (feature detection) and maps to canonical `TextAtPoint`:

Algorithm MVP:

* Try to get a caret/range at point:

  * if `document.caretRangeFromPoint` exists: use it
  * else if `document.caretPositionFromPoint` exists: use it
* If you get a text node + offset:

  * extract the full text node string
  * expand left/right from offset to word boundaries (letters/digits/apostrophes)
  * set `word`
  * set `surroundingText` to the text node value (MVP) or a slightly larger joined string later
* Anchor:

  * for MVP, use point anchor `{x: trigger.mouse.x, y: trigger.mouse.y}`
  * later, compute rect from range.getBoundingClientRect()

Return `null` if no text found.

### 14) `tooltipRenderer.ts`

Implements rendering the `DisplayInstruction` mode `TOOLTIP`:

* create a shadow DOM container singleton
* position near anchor point/rect
* show payload text
* dismiss on outside click and Escape (per instruction)

### 15) `rendererBridge.ts`

Implements Application `Renderer` contract in content context:

* `render(instruction, payload)` delegates to tooltip renderer (ignore video overlay for MVP)
* `dismiss()` hides tooltip

**Acceptance**

* When background returns OK with instruction TOOLTIP, user sees tooltip at click point.

---

## Options and Popup (minimal)

### 16) `options.ts`

* On load: `GET_SETTINGS` and populate fields
* Save: `SAVE_SETTINGS {settings}`

Fields minimum:

* sourceLanguageId
* targetLanguageId
* mouseButton + modifiers (default alt true)
* api base url + paths (if not hardcoded)

### 17) `popup.ts`

* Buttons:

  * Login → `LOGIN`
  * Logout → `LOGOUT`
  * Get Languages (optional debug) → `GET_LANGUAGES`
* Display session state:

  * call `GET_SETTINGS` + maybe a `GET_SESSION_STATE` (optional; if you didn’t implement, just infer from login response)

---

## Acceptance criteria for Step 5 (MVP definition)

1. Chrome extension loads.
2. Options page saves mapping + language ids.
3. Popup login stores tokens.
4. On any page, clicking with the configured hotkey results in:

   * content script sends canonical objects
   * background runs `HandleTrigger` and posts segment
   * response returns translation text (or placeholder)
   * tooltip appears at click point

(If your backend does not yet return translation text, render a placeholder like “Saved” and include returned segmentId.)

---

## Notes for the agent

* Keep YouTube/Netflix probes out of Step 5; generic only.
* If your Step 4 `HandleTrigger` currently calls `Renderer`, inject a NoopRenderer in background and still return the instruction/payload so content renders. The preferred end state is: rendering happens in content only.
