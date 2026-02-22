## General concept and rules (so the agent implements consistently)

### Architecture rules

1. **Flow:** `Infrastructure → Application → Domain` only.
2. **Domain/Application input/output types are canonical** and never include:

   * `chrome.*`, `browser.*`
   * `MouseEvent`, `HTMLElement`, `Range`, `Selection`
3. Infrastructure is responsible for:

   * capturing raw events
   * reading DOM or extension APIs
   * **mapping** browser/DOM specifics into canonical domain objects
4. Application contains only **use cases** and **contracts (interfaces)** for dependencies; use cases orchestrate.
5. Domain contains:

   * stable models (SegmentBundle, Language, Source…)
   * canonical inputs/outputs (Trigger, Snapshots, CaptureResult, DisplayInstruction)
   * strategy logic over canonical inputs (no DOM reads)

### Implementation rules

* Every file the agent creates must compile.
* Start with types first, then contracts, then one minimal vertical slice.
* Keep “step 1” narrowly scoped and testable (compile-time acceptance).

---

## Step 1 (only): Implement Domain canonical models and DTOs

### Objective

Create the minimal set of **Domain types** that everything else will use, matching:

* your backend `SegmentBundleDto`
* the canonical “trigger/snapshot/result” shapes for strategies

No logic yet. Only types + basic constructors/validators if needed.

### Files to create (exact paths)

Under `browser-extension/domain/src/`:

1. `model/Language.ts`
2. `model/Source.ts`
3. `model/SourceMetadata.ts`
4. `model/SegmentBundle.ts`
5. `input/Anchor.ts`
6. `input/Trigger.ts`
7. `input/SelectionSnapshot.ts`
8. `input/TextAtPoint.ts`
9. `input/SubtitleSnapshot.ts`
10. `model/CaptureResult.ts`
11. `model/DisplayInstruction.ts`
12. `index.ts` (re-export public domain API)

### Exact contents (what each contains)

#### 1) `model/Language.ts`

* `LanguageDto` interface with the fields your API returns (use common defaults if unknown).
* A small type guard or validator (optional).

**Include:**

* `id: string`
* `code: string` (BCP-47 like `en`, `it`, `en-US`)
* `name: string`

#### 2) `model/Source.ts`

* `SourceDto` union type or enum:

  * `WEB_PAGE`
  * `YOUTUBE`
  * `NETFLIX`
  * `UNKNOWN`

#### 3) `model/SourceMetadata.ts`

* `SourceMetadataDto` interface with optional fields typically useful:

  * `url: string` (required)
  * `title?: string`
  * `hostname?: string`
  * `path?: string`
  * `elementHint?: string` (like css selector or tag name; keep string-only)

#### 4) `model/SegmentBundle.ts`

* `SegmentBundleDto` interface matching your backend record:

Fields:

* `word: string`
* `sentence: string`
* `sourceLanguage: LanguageDto`
* `targetLanguage: LanguageDto`
* `source: SourceDto`
* `sourceMetadata?: SourceMetadataDto`
* `occurredAt: string` (ISO instant)

Also define:

* `type InstantString = string;` (document it as ISO-8601 UTC)

#### 5) `input/Anchor.ts`

Canonical placement anchor (no DOM Rect type):

* `AnchorPoint { kind: "point"; x: number; y: number }`
* `AnchorRect { kind: "rect"; x: number; y: number; w: number; h: number }`
* `type Anchor = AnchorPoint | AnchorRect`

#### 6) `input/Trigger.ts`

Canonical user action:

* `url: string`
* `mouse: { button: "left"|"middle"|"right"; x: number; y: number }`
* `modifiers: { alt: boolean; ctrl: boolean; shift: boolean; meta: boolean }`
* `selectedText?: string`
* `occurredAtMs: number` (number, later converted to ISO by Application using Clock)

#### 7) `input/SelectionSnapshot.ts`

* `selectedText: string`
* `anchor?: Anchor`

#### 8) `input/TextAtPoint.ts`

* `word: string`
* `surroundingText?: string` (plain string for strategies to build a sentence)
* `anchor: Anchor`

#### 9) `input/SubtitleSnapshot.ts`

* `text: string`
* `anchor: Anchor`
* `provider: "youtube"|"netflix"|"unknown"`

#### 10) `model/CaptureResult.ts`

* `word: string`
* `sentence: string`
* `source: SourceDto`
* `sourceMetadata?: SourceMetadataDto`
* `anchor: Anchor`

#### 11) `model/DisplayInstruction.ts`

* `mode: "TOOLTIP"|"VIDEO_OVERLAY"|"POPUP_ONLY"`
* `anchor: Anchor`
* `dismissOn?: { outsideClick?: boolean; escape?: boolean; scroll?: boolean }`

#### 12) `index.ts`

Re-export all public types.

### Acceptance criteria for Step 1

* TypeScript build succeeds for the `domain` package/folder.
* No imports from extension APIs or DOM APIs.
* `SegmentBundleDto` matches your backend record shape (with `occurredAt` as ISO string).

### Notes for the agent (do not do yet)

* Do not implement strategies, probes, or use cases in this step.
* Do not add browser-specific or DOM-specific types.

If you want, I can provide the exact TypeScript code for these 12 files next; but per your “step-by-step” request, Step 1 is the work item.
