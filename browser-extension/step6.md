## General concept and rules (for Step 6)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. **Domain strategies** decide capture + display using canonical snapshots (`SubtitleSnapshot`, `TextAtPoint`, etc.).
3. **Infrastructure probes** are responsible for:

   * reading site DOM (YouTube/Netflix)
   * handling browser quirks (feature detection)
   * mapping to canonical objects
4. Step 6 extends the Chrome MVP by adding **site-specific probes** (YouTube first, Netflix optional) and **site-aware rendering** (video overlay).
5. No application changes unless strictly necessary; ideally only domain strategy improvements + infra probes/renderers.

### Implementation rules

* Add one site at a time.
* Always keep a fallback path to Generic capture when site probe fails.
* Use MutationObserver with cleanup to avoid leaks.
* Avoid brittle selectors; provide multiple selectors and “best-effort” matching.

---

## Step 6: Add YouTube site support (probe + overlay renderer) and wire it into the existing flow

### Objective

On `youtube.com/watch` (and `youtu.be`), the extension should:

* capture the current caption/subtitle line as the **sentence**
* use selection or word-at-point as **word**
* show result using a **video overlay** anchored near the captions region (not just click point)

This is achieved by:

* adding a YouTube DOM probe that produces `SubtitleSnapshot`
* adding a video overlay renderer that can execute `DisplayInstruction.mode = VIDEO_OVERLAY`
* updating probe resolution so YouTube pages run both generic probe and YouTube probe

---

## Files to create/update (Chrome infrastructure + domain strategy tweaks if needed)

### A) Chrome infrastructure: YouTube probe

1. `browser-extension/infrastructure/chrome/src/content/probes/youtubeProbe.ts` (new)
2. `browser-extension/infrastructure/chrome/src/content/probes/probeResolver.ts` (update)
3. `browser-extension/infrastructure/chrome/src/content/probes/genericPageProbe.ts` (update, to merge subtitle snapshot when present)

### B) Chrome infrastructure: video overlay renderer

4. `browser-extension/infrastructure/chrome/src/content/render/videoOverlayRenderer.ts` (new)
5. `browser-extension/infrastructure/chrome/src/content/render/rendererBridge.ts` (update: handle VIDEO_OVERLAY)

### C) Domain (only if needed)

6. `browser-extension/domain/src/strategies/youtube/YouTubeStrategy.ts` (verify it prefers `SubtitleSnapshot` and outputs `VIDEO_OVERLAY`)

---

## Detailed implementation requirements

### 1) `youtubeProbe.ts` — what it must contain

**Public API**

* `startYouTubeSubtitleObserver(): void` (optional)
* `stopYouTubeSubtitleObserver(): void` (optional)
* `getSubtitleSnapshot(trigger: Trigger): Promise<SubtitleSnapshot | null>`

**Core behavior**

* Identify YouTube caption text currently visible.
* Maintain the “latest subtitle text” with a MutationObserver.
* Provide an `Anchor` for placement:

  * Prefer an anchor near the captions container bounding rect.
  * If not found, fallback to trigger point.

**Selector strategy (best effort)**
YouTube caption DOM changes; implement a robust approach:

* Try multiple known containers for captions:

  * elements whose text updates frequently and are near bottom-center of video
  * use heuristics:

    * element is visible
    * contains short lines of text
    * updates during playback
* Practical MVP approach:

  * Look for elements with `class`/`id` containing `caption` / `captions` / `ytp-caption`
  * Then fallback to scanning for bottom overlay text nodes

**MutationObserver**

* Observe `document.body` (or a narrowed container once found)
* On each mutation, attempt to extract current caption text
* Store `lastText` and `lastAnchorRect`

**Return value mapping**
Return:

* `SubtitleSnapshot.text = lastText` (normalized)
* `SubtitleSnapshot.provider = "youtube"`
* `SubtitleSnapshot.anchor = rect anchor` if rect known else point anchor

**Cleanup**

* Ensure observer is created once and disconnected when:

  * navigating away (SPA route change)
  * extension disabled/unloaded (best-effort)

---

### 2) `probeResolver.ts` — what it must contain

A function that decides which probes to run based on URL:

* If URL is YouTube watch:

  * run generic probe (selection + text-at-point + page info)
  * run `youtubeProbe.getSubtitleSnapshot`
* Else:

  * generic only (Step 5 behavior)

Return merged snapshots:

* `snapshots.subtitle = youtubeSubtitle || null`

---

### 3) `genericPageProbe.ts` — update requirement

* Must continue to provide:

  * `pageInfo`
  * `selection`
  * `textAtPoint`
* It should accept “extra subtitle snapshot” to merge, or it should simply return base snapshots and let resolver merge.

Keep it simple:

* `genericPageProbe(trigger) -> baseSnapshots`
* resolver merges subtitle.

---

## Video overlay rendering

### 4) `videoOverlayRenderer.ts` — what it must contain

**Public API**

* `show(anchor: Anchor, text: string, options?: { dismissOnOutsideClick?: boolean; dismissOnEscape?: boolean }): void`
* `hide(): void`

**Rendering requirements**

* Create a single overlay container injected into page:

  * positioned fixed
  * high z-index
  * styled for readability on video
* Placement:

  * If `anchor.kind === "rect"`:

    * position above/below rect with collision handling
  * If point:

    * position relative to point
* Dismiss logic:

  * Escape key hides if enabled
  * Outside click hides if enabled

---

### 5) `rendererBridge.ts` — update requirement

Currently (Step 5) it likely supports only TOOLTIP.
Update it so:

* if `instruction.mode === "TOOLTIP"` → use tooltip renderer
* if `instruction.mode === "VIDEO_OVERLAY"` → use video overlay renderer
* if `POPUP_ONLY` → do nothing in content

---

## Domain strategy check (minimal changes)

### 6) `YouTubeStrategy.ts`

Ensure:

* When `snapshots.subtitle.provider === "youtube"` and text exists:

  * sentence = subtitle.text
  * anchor = subtitle.anchor
* word from selection else textAtPoint.word
* display mode = `VIDEO_OVERLAY`

No DOM changes needed.

---

## Acceptance criteria for Step 6

1. On YouTube watch pages with captions visible:

   * `subtitleSnapshot` returns non-empty text that updates as captions change.
2. Clicking with hotkey:

   * sentence uses current caption line (not page paragraph context)
   * result is displayed using `VIDEO_OVERLAY` near captions region.
3. On non-YouTube pages:

   * behavior remains the Step 5 generic tooltip.
4. No memory leaks:

   * MutationObserver is not duplicated endlessly; it reuses one instance.

---

## Notes for the agent

* YouTube is a SPA; consider detecting URL changes:

  * listen to `history.pushState`/`popstate` or periodically check location.href
  * when URL changes away from watch, stop observer.
* Keep selectors tolerant; log debug info behind a settings flag if needed.
