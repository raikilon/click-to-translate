import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthSessionService } from '../../authentication/application/auth-session.service';
import { HighlightPreferenceService } from '../../authentication/application/highlight-preference.service';
import { HighlightMode } from '../../authentication/domain/highlight-mode';

@Component({
  selector: 'app-settings-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './settings-page.component.html'
})
export class SettingsPageComponent {
  constructor(
    protected readonly highlightPreferenceService: HighlightPreferenceService,
    private readonly authSessionService: AuthSessionService,
    private readonly router: Router
  ) {}

  backToHome(): void {
    void this.router.navigate(['/']);
  }

  updateMode(mode: HighlightMode): void {
    this.highlightPreferenceService.setMode(mode);
  }

  logout(): void {
    this.authSessionService.logout();
    void this.router.navigate(['/login']);
  }
}
