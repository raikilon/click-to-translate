import { storage } from "wxt/utils/storage";

export const highlightStyleIdStorageItem =
  storage.defineItem<string>("local:highlightStyleId", {
    fallback: "default",
  });




