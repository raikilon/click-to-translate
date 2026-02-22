## General concept and rules (for Step 3)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. **Domain:** models + pure strategy logic over canonical objects. No DOM/extension APIs.
3. **Application:** use cases orchestrate; depend only on contracts (interfaces) + domain types.
4. **Infrastructure:** implements contracts, does DOM/extension API work, maps to canonical objects.

### Implementation rules

* Implement one vertical slice at a time.
* Prefer use cases that are deterministic and testable with mocks.
* Background/service worker should remain a thin router; orchestration belongs to use cases.
* Avoid adding any new contracts unless strictly required by a use case.

---

## Step 3: Implement Domain strategy system (resolver + 3 strategies)

### Objective

Create the **domain strategy layer** that decides:

* how to compute `CaptureResult` from `Trigger + Snapshots`
* how to compute `DisplayInstruction` for that capture
* how to resolve the correct strategy for a URL

No DOM reads and no rendering side-effects.

### Files to create/update (exact paths)

Under `browser-extension/domain/src/`:

1. `strategies/Strategy.ts`
2. `strategies/StrategyResolver.ts`
3. `strategies/generic/GenericStrategy.ts`
4. `strategies/youtube/YouTubeStrategy.ts`
5. `strategies/netflix/NetflixStrategy.ts`
6. `strategies/index.ts`
7. Update `domain/src/index.ts` to export strategies

---

## Canonical “inputs” used by strategies

Strategies must accept a single object containing the optional snapshots:

```ts
type Snapshots = {
  pageInfo?: PageInfo;
  selection?: SelectionSnapshot | null;
  textAtPoint?: TextAtPoint | null;
  subtitle?: SubtitleSnapshot | null;
};
```

Infrastructure/Application will populate whichever snapshots are available; strategies must handle missing ones.

---

## Exact contents (what each file contains)

### 1) `strategies/Strategy.ts`

Define:

* `StrategyId = "generic" | "youtube" | "netflix"`
* `Snapshots` type (as above)
* `Strategy` interface:

Methods:

* `id: StrategyId`
* `matches(url: string): boolean`
* `computeCapture(trigger: Trigger, snapshots: Snapshots): CaptureResult | null`
* `computeDisplay(capture: CaptureResult, trigger: Trigger, snapshots: Snapshots): DisplayInstruction`

Rules:

* Must not throw for missing snapshots; return null if cannot capture.

---

### 2) `strategies/StrategyResolver.ts`

Define a resolver that:

* takes an array of strategies (or uses defaults)
* given `url` + optional overrides, returns a `Strategy`

Inputs:

* `url: string`
* `overrides?: Array<{ pattern: string; strategyId: StrategyId }>` (pattern is a simple substring or wildcard; keep it simple now)

Resolution order:

1. If any override pattern matches URL, use that strategyId.
2. Else pick first strategy where `matches(url)` is true.
3. Else fallback to `generic`.

Implementation detail:

* Keep pattern matching simple: substring match or `*` wildcard converted to regex.
* Do not depend on `URL` class unless you want; string is enough.

---

### 3) `strategies/generic/GenericStrategy.ts`

Goal: works everywhere.

Capture rules (priority):

1. If `snapshots.selection?.selectedText` exists and looks like a single “word/phrase” (non-empty), use as `word`.
2. Else if `snapshots.textAtPoint?.word` exists, use that.

Sentence rules:

* If `snapshots.subtitle?.text` exists AND provider is unknown? (ignore for generic)
* Prefer `snapshots.textAtPoint?.surroundingText`:

  * Use `contextWindow` rule to build sentence around `word` from the surrounding text if possible.
* Else fallback: `sentence = word`.

Source:

* `WEB_PAGE`

Anchor:

* Prefer:

  * selection.anchor if present
  * else textAtPoint.anchor
  * else create a point anchor from trigger mouse coords

Display:

* `TOOLTIP` anchored at chosen anchor
* dismiss defaults: outsideClick + escape

---

### 4) `strategies/youtube/YouTubeStrategy.ts`

matches:

* URL contains `youtube.com/watch` or `youtu.be/`

Capture rules:

* If `snapshots.subtitle?.provider === "youtube"` and `subtitle.text` exists:

  * sentence = subtitle.text
  * anchor = subtitle.anchor
* Determine word:

  * selection first (if exists), else textAtPoint.word
  * if none, return null

Source:

* `YOUTUBE`

Metadata:

* leave empty here; strategies should not invent metadata; that will be added by Application based on PageInfo (later).

Display:

* `VIDEO_OVERLAY` anchored at subtitle.anchor if available, else fallback to trigger point.

---

### 5) `strategies/netflix/NetflixStrategy.ts`

matches:

* URL contains `netflix.com/watch`

Capture:

* prefer subtitle snapshot when provider is netflix for sentence+anchor
* word from selection or textAtPoint.word
* return null if no word

Source:

* `NETFLIX`

Display:

* `VIDEO_OVERLAY` anchored at subtitle anchor else trigger point

---

### 6) `strategies/index.ts`

Re-export:

* Strategy types
* Resolver
* Concrete strategies
* A helper to build default strategy list: `[new YouTubeStrategy(), new NetflixStrategy(), new GenericStrategy()]`

(Implementation may use functions instead of classes if you prefer; keep consistent.)

---

### 7) Update `domain/src/index.ts`

Export strategy modules from the domain public API.

---

## Acceptance criteria for Step 3

* Domain compiles with strategies.
* Given mocked snapshots, each strategy produces deterministic outputs:

  * YouTube uses subtitle text as sentence when provider matches
  * Generic falls back properly
* StrategyResolver returns correct strategy based on URL and overrides.
* No DOM or extension APIs used.

---

## Notes for the agent

* Keep sentence logic minimal now; improve later.
* Do not add rendering code. Strategies only return `DisplayInstruction`.
* Do not add application use cases yet; that is Step 4.
