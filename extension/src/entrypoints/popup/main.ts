import { BackgroundClientFactory } from "@/entrypoints/shared/messaging/client";
import { PopupPage } from "./PopupPage";
import "./style.css";

new PopupPage({
  client: BackgroundClientFactory.create(),
}).register();
