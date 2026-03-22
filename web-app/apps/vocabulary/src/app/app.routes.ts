import { Routes } from '@angular/router';
import { authGuard } from './authentication/presentation/auth.guard';
import { LoginPageComponent } from './authentication/presentation/login-page/login-page.component';
import { appRoutePaths } from './routing/route.constants';
import { SettingsPageComponent } from './settings/settings-page.component';
import { EntryDetailsPageComponent } from './vocabulary/presentation/details/entry-details-page.component';
import { VocabularyHomePageComponent } from './vocabulary/presentation/home/vocabulary-home-page/vocabulary-home-page.component';

export const routes: Routes = [
  {
    path: appRoutePaths.login,
    component: LoginPageComponent,
  },
  {
    path: appRoutePaths.home,
    canActivate: [authGuard],
    children: [
      {
        path: appRoutePaths.home,
        component: VocabularyHomePageComponent,
      },
      {
        path: appRoutePaths.entryDetails,
        component: EntryDetailsPageComponent,
      },
      {
        path: appRoutePaths.settings,
        component: SettingsPageComponent,
      },
    ],
  },
  {
    path: appRoutePaths.wildcard,
    redirectTo: appRoutePaths.home,
  },
];
