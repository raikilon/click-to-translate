import type { AuthFlow } from "../contracts/AuthFlow";
import type { AuthSessionStore } from "../contracts/AuthSessionStore";

export class LogoutUseCase {
  constructor(
    private readonly authSessionStore: AuthSessionStore,
    private readonly authFlow?: AuthFlow,
  ) {}

  async execute(): Promise<void> {
    const session = await this.authSessionStore.get();
    if (session && this.authFlow?.logoutRemote) {
      await this.authFlow.logoutRemote(session);
    }

    await this.authSessionStore.clear();
  }
}