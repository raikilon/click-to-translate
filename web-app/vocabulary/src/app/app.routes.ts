import { Routes } from '@angular/router';
import { authGuard } from './authentication/presentation/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () =>
      import('./authentication/presentation/login-page.component').then(
        (module) => module.LoginPageComponent
      )
  },
  {
    path: 'auth/callback',
    loadComponent: () =>
      import('./authentication/presentation/auth-callback-page.component').then(
        (module) => module.AuthCallbackPageComponent
      )
  },
  {
    path: '',
    canActivate: [authGuard],
    children: [
      {
        path: '',
        loadComponent: () =>
          import('./vocabulary/presentation/home/vocabulary-home-page.component').then(
            (module) => module.VocabularyHomePageComponent
          )
      },
      {
        path: 'entries/:entryId',
        loadComponent: () =>
          import('./vocabulary/presentation/details/entry-details-page.component').then(
            (module) => module.EntryDetailsPageComponent
          )
      },
      {
        path: 'settings',
        loadComponent: () =>
          import('./settings/presentation/settings-page.component').then(
            (module) => module.SettingsPageComponent
          )
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
