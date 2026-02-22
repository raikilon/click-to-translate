import { registerOptions } from "@infra-shared/pages/options";
import { firefoxAdapter } from "../../platform/firefoxAdapter";

registerOptions(firefoxAdapter.runtime);
