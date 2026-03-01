import { TriggerStateMachine } from "./TriggerStateMachine";
import type { ILookupRuntime } from "@/content/lookup/application/ILookupRuntime";
import { StartContentCaptureUseCase } from "@/content/content-capture/application/StartContentCaptureUseCase";
import { StopContentCaptureUseCase } from "@/content/content-capture/application/StopContentCaptureUseCase";

export class ContentController implements ILookupRuntime {
  private static active = false;

  private started = false;

  private readonly onKeyDownHandler = this.onKeyDown.bind(this);
  private readonly onKeyUpHandler = this.onKeyUp.bind(this);
  private readonly onMouseMoveHandler = this.onMouseMove.bind(this);
  private readonly onClickHandler = this.onClick.bind(this);
  private readonly onBlurHandler = this.onBlur.bind(this);
  private readonly onVisibilityChangeHandler = this.onVisibilityChange.bind(this);

  constructor(
    private readonly stateMachine: TriggerStateMachine,
    private readonly startContentCaptureUseCase: StartContentCaptureUseCase,
    private readonly stopContentCaptureUseCase: StopContentCaptureUseCase,
  ) {}

  start(): void {
    if (this.started || ContentController.active) {
      return;
    }

    this.started = true;
    ContentController.active = true;

    this.startContentCaptureUseCase.execute();

    window.addEventListener("keydown", this.onKeyDownHandler);
    window.addEventListener("keyup", this.onKeyUpHandler);
    window.addEventListener("mousemove", this.onMouseMoveHandler, { passive: true });
    window.addEventListener("click", this.onClickHandler, true);
    window.addEventListener("blur", this.onBlurHandler);
    document.addEventListener("visibilitychange", this.onVisibilityChangeHandler);
  }

  stop(): void {
    if (!this.started) {
      return;
    }

    this.started = false;
    ContentController.active = false;

    this.stopContentCaptureUseCase.execute();

    window.removeEventListener("keydown", this.onKeyDownHandler);
    window.removeEventListener("keyup", this.onKeyUpHandler);
    window.removeEventListener("mousemove", this.onMouseMoveHandler);
    window.removeEventListener("click", this.onClickHandler, true);
    window.removeEventListener("blur", this.onBlurHandler);
    document.removeEventListener("visibilitychange", this.onVisibilityChangeHandler);
    this.stateMachine.onEscape();
  }

  private onKeyDown(event: KeyboardEvent): void {
    if (event.key === "Escape") {
      this.stateMachine.onEscape();
      return;
    }

    this.stateMachine.onKeyDown(event);
  }

  private onKeyUp(event: KeyboardEvent): void {
    this.stateMachine.onKeyUp(event);
  }

  private onMouseMove(event: MouseEvent): void {
    this.stateMachine.onMouseMove(event);
  }

  private onClick(event: MouseEvent): void {
    this.stateMachine.onClick(event);
  }

  private onBlur(): void {
    this.stateMachine.onBlur();
  }

  private onVisibilityChange(): void {
    if (document.hidden) {
      this.stateMachine.onHidden();
    }
  }
}





