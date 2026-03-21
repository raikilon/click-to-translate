import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthSessionService } from '../authentication/application/auth-session.service';
import { appRouteCommands } from '../routing/route.constants';
import { HighlightPreferenceService } from './preferences/highlight-preference.service';
import { HighlightMode } from './preferences/highlight-mode';

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
    void this.router.navigate(appRouteCommands.home());
  }

  updateMode(mode: HighlightMode): void {
    this.highlightPreferenceService.setMode(mode);
  }

  logout(): void {
    void this.performLogout();
  }

  private async performLogout(): Promise<void> {
    await this.authSessionService.logout();
    await this.router.navigate(appRouteCommands.login());
  }
}
