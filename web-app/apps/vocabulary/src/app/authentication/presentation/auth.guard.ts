import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { appRouteCommands, appRouteUrls } from '../../routing/route.constants';
import { AuthSessionService } from '../application/auth-session.service';

export const authGuard: CanActivateFn = (_route, state) => {
  const authSessionService = inject(AuthSessionService);
  const router = inject(Router);

  if (state.url === appRouteUrls.login) {
    return true;
  }

  return authSessionService
    .isAuthenticated()
    .then((isAuthenticated) =>
      isAuthenticated ? true : router.createUrlTree(appRouteCommands.login()),
    )
    .catch(() => router.createUrlTree(appRouteCommands.login()));
};
