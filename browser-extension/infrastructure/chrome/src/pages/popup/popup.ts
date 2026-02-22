import { registerPopup } from "@infra-shared/pages/popup";
import { chromeAdapter } from "../../platform/chromeAdapter";

registerPopup({
  runtime: chromeAdapter.runtime,
  storage: chromeAdapter.storage,
});
