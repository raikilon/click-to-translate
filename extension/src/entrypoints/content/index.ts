import { ContentScript } from "./ContentScript";
import { GenericPointCapture } from "./capture/GenericPointCapture";
import { SubtitleContextService } from "./capture/SubtitleContextService";
import { BackgroundClientFactory } from "@/entrypoints/shared/messaging/client";
import { PageRenderer } from "./render/PageRenderer";

export default defineContentScript({
  matches: ["<all_urls>"],
  runAt: "document_idle",
  main() {
    const subtitleContextService = new SubtitleContextService();
    subtitleContextService.initialize();

    new ContentScript({
      client: BackgroundClientFactory.create(),
      pointCapture: new GenericPointCapture(() =>
        subtitleContextService.getBufferedContext()
      ),
      renderer: new PageRenderer(),
    }).register();
  },
});
