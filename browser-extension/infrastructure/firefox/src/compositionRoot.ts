import {
  createCompositionRoot as createSharedCompositionRoot,
  type CompositionRoot,
} from "@infra-shared/compositionRoot";
import { firefoxAdapter } from "./platform/firefoxAdapter";

export type { CompositionRoot };

export function createCompositionRoot(): CompositionRoot {
  return createSharedCompositionRoot(firefoxAdapter);
}
