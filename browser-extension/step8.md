## General concept and rules (for Step 8)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. **Domain** and **Application** are reused as-is.
3. **Infrastructure is duplicated per browser**; only browser-specific pieces change:

   * manifest format/permissions differences
   * background runtime differences (MV3 behavior)
   * identity/OAuth API differences
   * messaging API differences (`chrome.*` vs `browser.*`)
4. Page DOM/site probes and renderers should remain the same behavior. If there are browser DOM quirks, fix them inside the **Firefox infrastructure probes**.

### Implementation rules

* Start by copying the entire Chrome infrastructure folder to Firefox.
* Then adjust only what breaks compilation/runtime.
* Keep message types identical to avoid application changes.
* Use `webextension-polyfill` if you want, but since you allow duplication, you can implement Firefox infra directly with `browser.*` APIs.

---

## Step 8: Firefox infrastructure target (duplicate Chrome infra, adjust browser-specific entities)

### Objective

Produce a Firefox build under `browser-extension/infrastructure/firefox` that:

* uses the same Domain and Application code
* supports:

  * options save
  * popup login/logout
  * content click capture + probes (generic + YouTube + Netflix)
  * background orchestration via use cases
  * tooltip/video overlay rendering

---

## Work items (exact, actionable checklist)

### A) Create Firefox infrastructure skeleton

1. Copy `browser-extension/infrastructure/chrome/` → `browser-extension/infrastructure/firefox/`.
2. Replace `manifest.json` with Firefox-compatible values.

---

### B) Manifest adjustments

3. Update `browser-extension/infrastructure/firefox/manifest.json`:

   * Ensure background is configured correctly for Firefox MV3 (or MV2 if you choose; prefer MV3 if supported in your target Firefox version).
   * Set `browser_specific_settings`:

     * `gecko.id` (extension id)
     * `gecko.strict_min_version` (optional)
   * Ensure permissions:

     * `storage`
     * identity/web-auth flow permissions as required in Firefox
   * Keep host permissions for API/auth origins.

**Acceptance**

* Firefox can load the extension temporarily in `about:debugging`.

---

### C) Replace extension API calls (chrome → browser)

4. Replace all `chrome.runtime.*` with `browser.runtime.*` (or polyfill).
5. Replace all `chrome.storage.local` with `browser.storage.local`.
6. Replace `chrome.identity.launchWebAuthFlow` with:

   * `browser.identity.launchWebAuthFlow` if available, OR
   * a fallback approach (see section D).

**Acceptance**

* TypeScript compiles for Firefox target.

---

### D) OAuth2 PKCE login in Firefox (critical difference)

7. Implement `infrastructure/firefox/src/impl/authFlow.ts` with one of these approaches:

#### Preferred: `browser.identity.launchWebAuthFlow`

* Same logic as Chrome:

  * PKCE + state
  * open auth window
  * receive redirect URL
  * token exchange

#### If identity API is not available/insufficient

* Use a dedicated extension page as redirect handler:

  * open auth URL in a new tab/window with redirect to an extension URL
  * the redirect handler page extracts `code` and sends it to background via `runtime.sendMessage`
  * background exchanges `code` for tokens

**Acceptance**

* Login works and stores tokens.

---

### E) Background message router parity

8. Ensure `background/background.ts` in Firefox:

   * registers `browser.runtime.onMessage.addListener`
   * returns Promises correctly (Firefox expects returned promise or explicit `sendResponse` handling depending on API usage)

**Acceptance**

* Popup/options/content can call background and get responses.

---

### F) Content scripts parity (usually minimal changes)

9. Ensure `content/contentScript.ts` sends messages via `browser.runtime.sendMessage`.
10. Confirm probes work in Firefox:

* caret logic:

  * ensure feature detection includes `document.caretPositionFromPoint`
  * keep the canonical mapping the same

11. Renderers unchanged except for any API calls (usually none).

**Acceptance**

* Generic capture works on text pages.
* Tooltip appears.

---

### G) Site probes parity verification

12. Verify YouTube probe in Firefox:

* captions extraction + observer works

13. Verify Netflix probe in Firefox:

* subtitle extraction + observer works

If one fails due to DOM API differences:

* fix inside `infrastructure/firefox/src/content/probes/*` only
* do not change domain/application.

**Acceptance**

* Same behavior as Chrome on YouTube/Netflix.

---

### H) Packaging/testing checklist

14. Test matrix in Firefox:

* Options save/load
* Login/logout
* Capture on a normal article page
* YouTube captions enabled → overlay render
* Netflix subtitles enabled → overlay render
* Token refresh path (shorten expiry in dev or simulate)

**Acceptance**

* Feature parity with Chrome.

---

## Deliverables at the end of Step 8

* `browser-extension/infrastructure/firefox/manifest.json` working
* Firefox-specific implementations for:

  * storage
  * messaging
  * auth flow
* All probes/renderers functional
* No changes required to Domain/Application for Firefox support
