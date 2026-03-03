import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';
import { AuthSessionService } from '../application/auth-session.service';

@Component({
  selector: 'app-auth-callback-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './auth-callback-page.component.html'
})
export class AuthCallbackPageComponent {
  protected readonly errorMessage = signal<string | null>(null);

  constructor(
    private readonly authSessionService: AuthSessionService,
    private readonly router: Router
  ) {
    void this.completeAuthentication();
  }

  private async completeAuthentication(): Promise<void> {
    try {
      await this.authSessionService.completeLogin(window.location.href);
      const redirectUrl = this.authSessionService.consumePostLoginRedirect() ?? '/';
      await this.router.navigateByUrl(redirectUrl);
    } catch (error) {
      this.errorMessage.set(this.asErrorMessage(error));
      await this.router.navigateByUrl('/login');
    }
  }

  private asErrorMessage(error: unknown): string {
    if (error instanceof Error) {
      return error.message;
    }

    return 'Authentication callback failed.';
  }
}
