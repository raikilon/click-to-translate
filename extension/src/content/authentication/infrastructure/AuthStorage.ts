import { storage } from "wxt/utils/storage";

export const authLogoutStorageItem =
  storage.defineItem<boolean>("local:authLogout", {
    fallback: false,
  });




