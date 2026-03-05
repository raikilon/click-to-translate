import { Routes } from '@angular/router';
import { AuthCallbackPageComponent } from './authentication/presentation/auth-callback-page.component';
import { authGuard } from './authentication/presentation/auth.guard';
import { LoginPageComponent } from './authentication/presentation/login-page.component';
import { SettingsPageComponent } from './settings/settings-page.component';
import { EntryDetailsPageComponent } from './vocabulary/presentation/details/entry-details-page.component';
import { VocabularyHomePageComponent } from './vocabulary/presentation/home/vocabulary-home-page.component';

export const routes: Routes = [
  {
    path: 'login',
    component: LoginPageComponent
  },
  {
    path: 'auth/callback',
    component: AuthCallbackPageComponent
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        component: VocabularyHomePageComponent
      },
      {
        path: 'entries/:entryId',
        component: EntryDetailsPageComponent
      },
      {
        path: 'settings',
        component: SettingsPageComponent
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
