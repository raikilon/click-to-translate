import type { IAuthenticationService } from "./IAuthenticationService";

export class LogoutUseCase {
  constructor(private readonly authenticationService: IAuthenticationService) {}

  execute(): Promise<void> {
    return this.authenticationService.logout();
  }
}
