## General concept and rules (for Step 7)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. **Domain** does not touch DOM; it consumes canonical snapshots (including `SubtitleSnapshot`) and decides capture + display.
3. **Infrastructure**:

   * reads Netflix DOM
   * handles any browser quirks
   * maps to canonical `SubtitleSnapshot` + `Anchor`
4. Netflix logic must be **safe-by-default**:

   * if subtitles are not detected, fall back to generic behavior (Step 5)
   * never break playback controls (avoid `preventDefault` unless necessary)
5. Keep the change surface small:

   * add a Netflix probe and hook it into the existing probe resolver
   * reuse the existing `VIDEO_OVERLAY` renderer from Step 6

### Implementation rules

* Netflix is more DOM/SPA/AB-test heavy than YouTube:

  * use MutationObserver
  * use multiple selectors + heuristics
  * track latest subtitle text and anchor rect
* Prefer finding the subtitle container; if not found, use heuristics:

  * bottom-center region
  * visible text lines changing over time
* Add cleanup for observer on URL change.

---

## Step 7: Add Netflix site support (probe + wiring) using the existing video overlay renderer

### Objective

On `netflix.com/watch/...`:

* capture current subtitle line as **sentence**
* choose **word** from selection or word-at-point
* show translation using **VIDEO_OVERLAY** anchored near subtitle region

This requires:

* a Netflix DOM probe producing `SubtitleSnapshot(provider="netflix")`
* probe resolver wiring so Netflix pages include subtitle snapshots
* verifying Domain `NetflixStrategy` prefers subtitle snapshot and returns VIDEO_OVERLAY

---

## Files to create/update (Chrome infrastructure + domain check)

### A) Chrome infrastructure: Netflix probe

1. `browser-extension/infrastructure/chrome/src/content/probes/netflixProbe.ts` (new)
2. `browser-extension/infrastructure/chrome/src/content/probes/probeResolver.ts` (update)

### B) Domain (verify; change only if required)

3. `browser-extension/domain/src/strategies/netflix/NetflixStrategy.ts` (verify)

---

## Detailed implementation requirements

### 1) `netflixProbe.ts` — what it must contain

**Public API**

* `getSubtitleSnapshot(trigger: Trigger): Promise<SubtitleSnapshot | null>`
* (optional) `startNetflixSubtitleObserver()` / `stop...()` if you keep a long-lived observer

**Core behavior**

* Maintain `lastSubtitleText: string | null`
* Maintain `lastAnchor: Anchor | null`
* Update these whenever subtitle DOM changes.

**Netflix subtitle extraction approach (robust MVP)**

Use layered detection:

#### Layer A: Known selectors (fast path)

Try to find subtitle nodes using common Netflix patterns. Netflix changes often, so implement multiple attempts:

* elements with attributes/classnames containing keywords:

  * `subtitle`, `subtitles`, `player-timedtext`, `timedtext`, `caption`
* examine candidate elements whose innerText is non-empty and short (e.g., < 200 chars)

#### Layer B: Heuristic scan (fallback)

If no known selector matches:

* scan visible elements near the bottom third of viewport:

  * bounding rect `y` > 0.6 * viewport height
  * width not tiny, text not huge
  * element is displayed and opacity > 0
* pick the best candidate by a score:

  * centered horizontally
  * 1–2 lines of text
  * updates over time (tracked via observer)

**MutationObserver**

* Observe `document.body` (or the player root if found)
* On mutations, re-run extraction; if extracted text differs from `lastSubtitleText`, update stored values.

**Anchor**

* Prefer `rect` anchor using `element.getBoundingClientRect()` of the subtitle container.
* If you only have a text node, anchor to its parent element.
* If all else fails, use a point anchor at trigger coords.

**Return mapping**
Return `SubtitleSnapshot`:

* `text`: normalized subtitle string (trim, collapse whitespace)
* `provider`: `"netflix"`
* `anchor`: rect anchor if available else point

**Cleanup**
Netflix is SPA:

* Stop/restart observer when URL changes away from `/watch`
* Ensure you don’t create multiple observers:

  * singleton observer + guard flag

---

### 2) `probeResolver.ts` — update requirement

Add Netflix detection:

* If URL contains `netflix.com/watch`:

  * run generic snapshots
  * run `netflixProbe.getSubtitleSnapshot(trigger)`
  * merge `snapshots.subtitle`

Keep existing YouTube logic intact.

Pseudo resolution:

* if youtube → youtube probe
* else if netflix → netflix probe
* else none

---

### 3) Domain `NetflixStrategy.ts` — verification checklist

Ensure it does:

* `matches(url)` for Netflix watch pages
* capture:

  * sentence from `snapshots.subtitle` when provider == netflix
  * word from selection else textAtPoint.word
  * anchor from subtitle anchor when available
  * source = `NETFLIX`
* display:

  * `VIDEO_OVERLAY` (not tooltip)

No DOM changes.

---

## Acceptance criteria for Step 7

1. On Netflix watch pages with subtitles enabled:

   * `netflixProbe.getSubtitleSnapshot` returns non-empty text and a reasonable anchor rect.
2. Clicking with configured hotkey:

   * sentence uses current subtitle text
   * display uses `VIDEO_OVERLAY` near subtitle region
3. If subtitles are disabled or not detectable:

   * fall back to generic behavior (tooltip with surrounding text)
4. No duplicated observers during SPA navigation:

   * observer count remains 1 for the page.

---

## Notes for the agent

* Netflix sometimes renders timed text into elements that appear/disappear with player UI; the observer approach is required.
* Avoid using extremely brittle selectors; prefer heuristic scoring with fallback.
* Do not block click events globally; only handle your hotkey combination.
