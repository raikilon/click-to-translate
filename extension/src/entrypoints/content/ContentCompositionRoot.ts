import { ClickGate } from "@/content/lookup/infrastructure/ClickGate";
import { ContentController } from "@/content/lookup/infrastructure/ContentController";
import { ModifierKeyState } from "@/content/lookup/infrastructure/ModifierKeyState";
import { MouseIdleGate } from "@/content/lookup/infrastructure/MouseIdleGate";
import { TriggerStateMachine } from "@/content/lookup/infrastructure/TriggerStateMachine";
import { StartLookupUseCase } from "@/content/lookup/application/StartLookupUseCase";
import { StopLookupUseCase } from "@/content/lookup/application/StopLookupUseCase";
import { CaptureWordAtPointUseCase } from "@/content/content-capture/application/CaptureWordAtPointUseCase";
import { CaptureContextForWordUseCase } from "@/content/content-capture/application/CaptureContextForWordUseCase";
import { StartContentCaptureUseCase } from "@/content/content-capture/application/StartContentCaptureUseCase";
import { StopContentCaptureUseCase } from "@/content/content-capture/application/StopContentCaptureUseCase";
import { FixedWindowContextExtractor } from "@/content/content-capture/domain/FixedWindowContextExtractor";
import { SubtitleBuffer } from "@/content/content-capture/domain/SubtitleBuffer";
import { WordBoundaryFinder } from "@/content/content-capture/domain/WordBoundaryFinder";
import { CaretFromPointResolver } from "@/content/content-capture/infrastructure/CaretFromPointResolver";
import { RangeRectMeasurer } from "@/content/content-capture/infrastructure/RangeRectMeasurer";
import { DomContextLocator } from "@/content/content-capture/infrastructure/DomContextLocator";
import { DomWordLocator } from "@/content/content-capture/infrastructure/DomWordLocator";
import { CompositeContextLocator } from "@/content/content-capture/infrastructure/CompositeContextLocator";
import { CompositeWordLocator } from "@/content/content-capture/infrastructure/CompositeWordLocator";
import { SubtitleContextLocator } from "@/content/content-capture/infrastructure/SubtitleContextLocator";
import { BackgroundTranslationClient } from "@/content/translation/infrastructure/BackgroundTranslationClient";
import { TranslateWordUseCase } from "@/content/translation/application/TranslateWordUseCase";
import { BrowserPrefsRepository } from "@/content/lookup/infrastructure/BrowserPrefsRepository";
import { OverlayHighlighter } from "@/content/popup/infrastructure/OverlayHighlighter";
import { TranslationPopup } from "@/content/popup/infrastructure/TranslationPopup";
import { HighlightPrefsRepository } from "@/content/popup/infrastructure/HighlightPrefsRepository";
import { ShowHighlightUseCase } from "@/content/popup/application/ShowHighlightUseCase";
import { ClearHighlightUseCase } from "@/content/popup/application/ClearHighlightUseCase";
import { ShowTranslationPopupUseCase } from "@/content/popup/application/ShowTranslationPopupUseCase";
import { ClearTranslationPopupUseCase } from "@/content/popup/application/ClearTranslationPopupUseCase";
import { NetflixSubtitleWatcher } from "@/content/content-capture/infrastructure/NetflixSubtitleWatcher";
import { SubtitleBufferFeeder } from "@/content/content-capture/infrastructure/SubtitleBufferFeeder";
import { YouTubeSubtitleWatcher } from "@/content/content-capture/infrastructure/YouTubeSubtitleWatcher";
import { ContentCaptureRuntime } from "@/content/content-capture/infrastructure/ContentCaptureRuntime";

export class ContentCompositionRoot {
  build(): { start: StartLookupUseCase; stop: StopLookupUseCase } {
    const prefsRepository = new BrowserPrefsRepository();

    const caretResolver = new CaretFromPointResolver();
    const rectMeasurer = new RangeRectMeasurer();
    const wordBoundaryFinder = new WordBoundaryFinder();
    const contextExtractor = new FixedWindowContextExtractor();

    const domWordLocator = new DomWordLocator(caretResolver, wordBoundaryFinder, rectMeasurer);
    const domContextLocator = new DomContextLocator(contextExtractor);

    const subtitleBuffer = new SubtitleBuffer();
    const youtubeWatcher = new YouTubeSubtitleWatcher();
    const netflixWatcher = new NetflixSubtitleWatcher();
    const subtitleBufferFeeder = new SubtitleBufferFeeder(subtitleBuffer, [
      youtubeWatcher,
      netflixWatcher,
    ]);
    const contentCaptureRuntime = new ContentCaptureRuntime(subtitleBufferFeeder);
    const startContentCaptureUseCase = new StartContentCaptureUseCase(contentCaptureRuntime);
    const stopContentCaptureUseCase = new StopContentCaptureUseCase(contentCaptureRuntime);
    const subtitleContextLocator = new SubtitleContextLocator(subtitleBuffer);

    const wordLocator = new CompositeWordLocator([domWordLocator]);
    const contextLocator = new CompositeContextLocator([
      subtitleContextLocator,
      domContextLocator,
    ]);
    const captureWordAtPointUseCase = new CaptureWordAtPointUseCase(wordLocator);
    const captureContextForWordUseCase = new CaptureContextForWordUseCase(contextLocator);

    const highlighter = new OverlayHighlighter();
    const popup = new TranslationPopup();
    const highlightPrefsRepository = new HighlightPrefsRepository();
    const showHighlightUseCase = new ShowHighlightUseCase(
      highlighter,
      highlightPrefsRepository,
    );
    const clearHighlightUseCase = new ClearHighlightUseCase(highlighter);
    const showTranslationPopupUseCase = new ShowTranslationPopupUseCase(popup);
    const clearTranslationPopupUseCase = new ClearTranslationPopupUseCase(popup);

    const translationClient = new BackgroundTranslationClient();
    const translateWordUseCase = new TranslateWordUseCase(
      translationClient,
    );

    const modifierKeyState = new ModifierKeyState();
    const mouseIdleGate = new MouseIdleGate();
    const clickGate = new ClickGate();

    const triggerStateMachine = new TriggerStateMachine(
      modifierKeyState,
      mouseIdleGate,
      clickGate,
      captureWordAtPointUseCase,
      captureContextForWordUseCase,
      translateWordUseCase,
      showHighlightUseCase,
      clearHighlightUseCase,
      showTranslationPopupUseCase,
      clearTranslationPopupUseCase,
      prefsRepository,
    );

    const controller = new ContentController(
      triggerStateMachine,
      startContentCaptureUseCase,
      stopContentCaptureUseCase,
    );
    const start = new StartLookupUseCase(controller);
    const stop = new StopLookupUseCase(controller);
    return { start, stop };
  }
}





