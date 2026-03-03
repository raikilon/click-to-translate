import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthSessionService } from '../application/auth-session.service';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './login-page.component.html'
})
export class LoginPageComponent {
  protected readonly loading = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  constructor(
    private readonly authSessionService: AuthSessionService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    if (this.authSessionService.isAuthenticated()) {
      void this.router.navigateByUrl(this.readRedirectUrl());
    }
  }

  async login(): Promise<void> {
    this.loading.set(true);
    this.errorMessage.set(null);

    try {
      await this.authSessionService.beginLogin(this.readRedirectUrl());
    } catch (error) {
      this.errorMessage.set(this.asErrorMessage(error));
      this.loading.set(false);
    }
  }

  private readRedirectUrl(): string {
    return this.route.snapshot.queryParamMap.get('redirect') ?? '/';
  }

  private asErrorMessage(error: unknown): string {
    if (error instanceof Error) {
      return error.message;
    }

    return 'Login failed.';
  }
}
