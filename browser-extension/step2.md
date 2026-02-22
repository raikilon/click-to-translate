## General concept and rules (unchanged, restated for Step 2 context)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. **Domain**: canonical types + strategy logic. No extension APIs and no DOM types.
3. **Application**: use cases orchestrate; it depends on **interfaces** (contracts) for anything external.
4. **Infrastructure**: implements contracts using extension APIs/HTTP/storage; maps raw browser/DOM specifics into canonical domain objects.

### Implementation rules

* Keep steps small and compilable.
* Use cases should be testable by mocking contracts.
* No “helper service layer”; orchestration stays in use cases.

---

## Step 2: Implement Application contracts + settings/session models (no use cases yet)

### Objective

Create the **application boundary**: all interfaces and data models that use cases will rely on.
This makes DI explicit and tells the LLM exactly what infrastructure must implement.

### What to create (exact paths)

Under `browser-extension/application/src/`:

1. `contracts/SettingsStore.ts`
2. `contracts/AuthSessionStore.ts`
3. `contracts/AuthFlow.ts`
4. `contracts/ApiClient.ts`
5. `contracts/Clock.ts`
6. `contracts/PageProbe.ts`
7. `contracts/Renderer.ts`
8. `model/Settings.ts`
9. `model/AuthSession.ts`
10. `model/ApiModels.ts` (optional, but recommended)
11. `index.ts` (re-export public application API)

No use cases in this step.

---

## Exact contents (what each file contains)

### 1) `contracts/SettingsStore.ts`

An interface to persist settings.

Must include:

* `get(): Promise<Settings>`
* `save(settings: Settings): Promise<void>`

Notes:

* Infrastructure decides where (storage.local, etc.).
* Application does not know.

### 2) `contracts/AuthSessionStore.ts`

An interface to store session tokens.

Must include:

* `get(): Promise<AuthSession | null>`
* `set(session: AuthSession): Promise<void>`
* `clear(): Promise<void>`

### 3) `contracts/AuthFlow.ts`

An interface for interactive login (OAuth2 Code + PKCE happens in infrastructure).

Must include:

* `loginInteractive(): Promise<AuthSession>`
  Optionally:
* `logoutRemote?(session: AuthSession): Promise<void>` (if you support revocation)

### 4) `contracts/ApiClient.ts`

An interface for your backend endpoints + token refresh.

Must include:

* `getLanguages(accessToken: string | null): Promise<LanguageDto[]>`
* `postSegment(accessToken: string, bundle: SegmentBundleDto): Promise<PostSegmentResponse>`
* `refreshAccessToken(refreshToken: string): Promise<AuthSession>` (or a narrower token response)

`PostSegmentResponse` depends on your backend:

* If your segments endpoint returns translation immediately, model it here.
* If it returns ack only, model it accordingly.

Provide a default shape now (can be adjusted later):

* `translationText?: string`
* `segmentId?: string`

### 5) `contracts/Clock.ts`

* `nowMs(): number`
  (Use case will convert to ISO string as needed.)

### 6) `contracts/PageProbe.ts`

This is how application receives canonical snapshots (produced in infrastructure content scripts).

Because probes can be site-specific, keep it flexible and return `null` when not available.

Must include:

* `getPageInfo(): Promise<PageInfo>`
* `getSelectionSnapshot(trigger: Trigger): Promise<SelectionSnapshot | null>`
* `getTextAtPoint(trigger: Trigger): Promise<TextAtPoint | null>`
* `getSubtitleSnapshot(trigger: Trigger): Promise<SubtitleSnapshot | null>`

Important:

* This returns **domain canonical types only**.

### 7) `contracts/Renderer.ts`

How application asks infrastructure to render a result.

Must include:

* `render(instruction: DisplayInstruction, payload: RenderPayload): Promise<void>`
* `dismiss(): Promise<void>`

`RenderPayload` should at minimum include:

* `text: string`
  Optionally:
* `debug?: any` (avoid in production; keep typed if used)

### 8) `model/Settings.ts`

Settings shape used by use cases and configured by options UI.

Include:

* API:

  * `apiBaseUrl: string`
  * `languagesPath: string`
  * `segmentsPath: string`
* Auth:

  * `authAuthorizeUrl: string` (or base + derived)
  * `authTokenUrl: string`
  * `oauthClientId: string`
  * `scopes: string[]`
  * `registerUrl?: string`
* User selection:

  * `sourceLanguageId: string | null`
  * `targetLanguageId: string | null`
* Hotkey mapping:

  * `mouseButton: "left"|"middle"|"right"`
  * `modifiers: { alt: boolean; ctrl: boolean; shift: boolean; meta: boolean }`
* Strategy overrides (optional):

  * `siteOverrides?: Array<{ pattern: string; strategyId: "generic"|"youtube"|"netflix" }>`
* UI preferences (optional):

  * `showTooltip: boolean` (or derived from DisplayInstruction)

Also include:

* `export const DEFAULT_SETTINGS: Settings = ...` with default mapping `Alt + Left Click`.

### 9) `model/AuthSession.ts`

Session token model (kept application-level, stored by infrastructure).

Include:

* `accessToken: string`
* `refreshToken?: string`
* `expiresAtMs: number` (epoch ms)
* `idToken?: string` (optional)
* `tokenType?: "Bearer"` (optional)

### 10) `model/ApiModels.ts`

Types for API responses used in `ApiClient`.

Include:

* `PostSegmentResponse`
* Optionally `ApiError` shape

### 11) `index.ts`

Re-export contracts and models.

---

## Acceptance criteria for Step 2

* `application` compiles.
* It imports domain types (`Trigger`, `LanguageDto`, `SegmentBundleDto`, etc.) but not infrastructure.
* No DOM or extension APIs appear in Application code.
* `DEFAULT_SETTINGS` exists and sets `Alt + Left Click`.

---

## Guidance for the agent (important constraints)

* Do not implement token refresh logic yet; just define contract methods.
* Do not implement use cases yet.
* Do not introduce shared folders across browsers.
* Keep response models minimal; they can be expanded once backend response is confirmed.
