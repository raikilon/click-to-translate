import type { AuthState } from "@/content/authentication/domain/AuthState";
import type { IAuthenticationService } from "./IAuthenticationService";

export class GetAuthStateUseCase {
  constructor(private readonly authenticationService: IAuthenticationService) {}

  execute(): Promise<AuthState> {
    return this.authenticationService.getAuthState();
  }
}
