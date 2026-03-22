import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { Router } from '@angular/router';
import { appRouteUrls } from '../../../routing/route.constants';
import { AuthSessionService } from '../../application/auth-session.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login-page.component.html',
})
export class LoginPageComponent {
  protected readonly loading = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  constructor(
    private readonly authSessionService: AuthSessionService,
    private readonly router: Router,
  ) {
    void this.redirectToHomeWhenAuthenticated();
  }

  async login(): Promise<void> {
    this.loading.set(true);
    this.errorMessage.set(null);

    try {
      await this.authSessionService.beginLogin();
      await this.router.navigateByUrl(appRouteUrls.home);
    } catch (error) {
      this.errorMessage.set(this.asErrorMessage(error));
    } finally {
      this.loading.set(false);
    }
  }

  private async redirectToHomeWhenAuthenticated(): Promise<void> {
    const isAuthenticated = await this.authSessionService.isAuthenticated().catch(() => false);
    if (isAuthenticated) {
      await this.router.navigateByUrl(appRouteUrls.home);
    }
  }

  private asErrorMessage(error: unknown): string {
    if (error instanceof Error) {
      return error.message;
    }

    return 'Login failed.';
  }
}
