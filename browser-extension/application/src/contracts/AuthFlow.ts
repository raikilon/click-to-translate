import type { AuthSession } from "../model/AuthSession";

export interface AuthFlow {
  loginInteractive(): Promise<AuthSession>;
  logoutRemote?(session: AuthSession): Promise<void>;
}
