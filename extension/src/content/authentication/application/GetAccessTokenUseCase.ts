import type { IAuthenticationService } from "./IAuthenticationService";

export class GetAccessTokenUseCase {
  constructor(private readonly authenticationService: IAuthenticationService) {}

  execute(interactive = false): Promise<string | null> {
    return this.authenticationService.getAccessToken(interactive);
  }
}
