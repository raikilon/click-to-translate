import { ContentCompositionRoot } from "./ContentCompositionRoot";

export default defineContentScript({
  matches: ["<all_urls>"],
  runAt: "document_idle",
  main() {
    const lookup = new ContentCompositionRoot().build();
    lookup.start.execute();
  },
});





