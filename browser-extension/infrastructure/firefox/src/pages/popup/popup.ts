import { registerPopup } from "@infra-shared/pages/popup";
import { firefoxAdapter } from "../../platform/firefoxAdapter";

registerPopup({
  runtime: firefoxAdapter.runtime,
});
