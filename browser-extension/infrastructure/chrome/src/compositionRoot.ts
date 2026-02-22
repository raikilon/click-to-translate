import {
  createCompositionRoot as createSharedCompositionRoot,
  type CompositionRoot,
} from "@infra-shared/compositionRoot";
import { chromeAdapter } from "./platform/chromeAdapter";

export type { CompositionRoot };

export function createCompositionRoot(): CompositionRoot {
  return createSharedCompositionRoot(chromeAdapter);
}
