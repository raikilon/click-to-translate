/**
 * File role: Bootstraps popup UI behavior and connects it to the background client.
 * Why this shape: Popup entrypoint stays thin for WXT hot-reload/build behavior and clear separation from business logic.
 */
import { BackgroundClientFactory } from "@/entrypoints/shared/messaging/client";
import { PopupPage } from "./PopupPage";
import "./style.css";

new PopupPage({
  client: BackgroundClientFactory.create(),
}).register();
