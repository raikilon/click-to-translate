import type { EnsureAuthSessionUseCase } from "./EnsureAuthSessionUseCase";
import type { AuthSession } from "../model/AuthSession";

export class LoginUseCase {
  constructor(
    private readonly ensureAuthSession: EnsureAuthSessionUseCase,
  ) {}

  execute(): Promise<AuthSession | null> {
    return this.ensureAuthSession.execute({ interactive: true });
  }
}