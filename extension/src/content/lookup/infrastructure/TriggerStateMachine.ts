import type { TriggerPrefs } from "@/content/lookup/domain/TriggerPrefs";
import { CaptureWordAtPointUseCase } from "@/content/content-capture/application/CaptureWordAtPointUseCase";
import { CaptureContextForWordUseCase } from "@/content/content-capture/application/CaptureContextForWordUseCase";
import type { IPrefsRepository } from "@/content/lookup/application/IPrefsRepository";
import { CapturePoint } from "@/content/content-capture/domain/CapturePoint";
import type { CapturedWord } from "@/content/content-capture/domain/CapturedWord";
import { TranslateWordUseCase } from "@/content/translation/application/TranslateWordUseCase";
import { ShowHighlightUseCase } from "@/content/popup/application/ShowHighlightUseCase";
import { ClearHighlightUseCase } from "@/content/popup/application/ClearHighlightUseCase";
import { ShowTranslationPopupUseCase } from "@/content/popup/application/ShowTranslationPopupUseCase";
import { ClearTranslationPopupUseCase } from "@/content/popup/application/ClearTranslationPopupUseCase";
import { HighlightSelection } from "@/content/popup/domain/HighlightSelection";
import { TranslationPopupContent } from "@/content/popup/domain/TranslationPopupContent";
import { ModifierKeyState } from "./ModifierKeyState";
import { MouseIdleGate } from "./MouseIdleGate";
import { ClickGate } from "./ClickGate";

export class TriggerStateMachine {
  private armed = false;
  private capturedWord: CapturedWord | undefined;
  private triggerPrefs: TriggerPrefs | undefined;

  constructor(
    private readonly modifierKeyState: ModifierKeyState,
    private readonly mouseIdleGate: MouseIdleGate,
    private readonly clickGate: ClickGate,
    private readonly captureWordAtPointUseCase: CaptureWordAtPointUseCase,
    private readonly captureContextForWordUseCase: CaptureContextForWordUseCase,
    private readonly translateWordUseCase: TranslateWordUseCase,
    private readonly showHighlightUseCase: ShowHighlightUseCase,
    private readonly clearHighlightUseCase: ClearHighlightUseCase,
    private readonly showTranslationPopupUseCase: ShowTranslationPopupUseCase,
    private readonly clearTranslationPopupUseCase: ClearTranslationPopupUseCase,
    private readonly prefsRepository: IPrefsRepository,
  ) {}

  onKeyDown(event: KeyboardEvent): void {
    this.modifierKeyState.onKeyDown(event);
    void this.refreshArmState();
  }

  onKeyUp(event: KeyboardEvent): void {
    this.modifierKeyState.onKeyUp(event);
    void this.refreshArmState();
  }

  onMouseMove(event: MouseEvent): void {
    if (!this.armed) {
      return;
    }

    this.mouseIdleGate.onMouseMove(new CapturePoint(event.clientX, event.clientY));
  }

  onMouseIdle(point: CapturePoint): void {
    if (!this.armed) {
      return;
    }

    const match = this.captureWordAtPointUseCase.execute(point);
    if (!match) {
      this.clearHighlightUseCase.execute();
      this.capturedWord = match;
      return;
    }

    this.capturedWord = match;
    void this.showHighlightUseCase.execute(
      new HighlightSelection(match.textNode, match.start, match.end),
    );
  }

  onClick(event: MouseEvent): void {
    if (!this.armed || !this.capturedWord) {
      return;
    }

    if (!this.triggerPrefs || !this.clickGate.matches(event, this.triggerPrefs)) {
      return;
    }

    this.consumeClick(event);

    const contextMatch = this.captureContextForWordUseCase.execute(this.capturedWord);
    if (!contextMatch) {
      this.clearTranslationPopupUseCase.execute();
      return;
    }

    void this.translateWordUseCase
      .execute(contextMatch.word, contextMatch.context)
      .then((translation) => {
      if (!translation) {
        this.clearTranslationPopupUseCase.execute();
        return;
      }

      const popupContent = new TranslationPopupContent(
        contextMatch.anchorX,
        contextMatch.anchorY,
        translation,
      );
      this.showTranslationPopupUseCase.execute(popupContent);
    });
  }

  onEscape(): void {
    this.disarm();
  }

  onBlur(): void {
    this.disarm();
    this.modifierKeyState.reset();
  }

  onHidden(): void {
    this.disarm();
  }

  private async refreshArmState(): Promise<void> {
    const triggerPrefs = await this.prefsRepository.getTriggerPrefs();
    this.triggerPrefs = triggerPrefs;
    const shouldArm = this.modifierKeyState.matches(triggerPrefs);

    if (shouldArm && !this.armed) {
      const hoverDelayMs = await this.prefsRepository.getHoverDelayMs();
      this.armed = true;
      this.mouseIdleGate.arm(hoverDelayMs, this.onMouseIdle.bind(this));
      return;
    }

    if (!shouldArm && this.armed) {
      this.disarm();
    }
  }

  private disarm(): void {
    this.armed = false;
    this.capturedWord = undefined;
    this.mouseIdleGate.disarm();
    this.clearHighlightUseCase.execute();
    this.clearTranslationPopupUseCase.execute();
  }

  private consumeClick(event: MouseEvent): void {
    event.preventDefault();
    event.stopPropagation();
    event.stopImmediatePropagation();
  }
}





