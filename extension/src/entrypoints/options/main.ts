/**
 * File role: Bootstraps options UI behavior.
 * Why this shape: Options entrypoint is kept minimal so WXT page lifecycle concerns do not leak into infra logic.
 */
import { OptionsPage } from "./OptionsPage";
import "./style.css";

new OptionsPage().register();
