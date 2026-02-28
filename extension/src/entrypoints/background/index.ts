/**
 * File role: Bootstraps the WXT background entrypoint and registers background services.
 * Why this shape: WXT expects thin entrypoints, so this file wires infrastructure and keeps startup synchronous.
 */
import { Background } from "./Background";

export default defineBackground(() => {
  new Background().register();
});
