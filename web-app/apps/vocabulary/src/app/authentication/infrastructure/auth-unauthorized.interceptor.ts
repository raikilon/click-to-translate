import { HttpErrorResponse, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { appRouteUrls } from '../../routing/route.constants';
import { AuthSessionService } from '../application/auth-session.service';

const authEndpointsPrefix = '/auth';

let redirectInFlight: Promise<boolean> | null = null;

export const authUnauthorizedInterceptor: HttpInterceptorFn = (request, next) => {
  const router = inject(Router);
  const authSessionService = inject(AuthSessionService);

  return next(request).pipe(
    catchError((error: unknown) => {
      if (isUnauthorizedForBackendRequest(error, request)) {
        authSessionService.clearAuthState();
        redirectToLogin(router);
      }

      return throwError(() => error);
    }),
  );
};

function isUnauthorizedForBackendRequest(error: unknown, request: HttpRequest<unknown>): boolean {
  return (
    error instanceof HttpErrorResponse &&
    error.status === 401 &&
    isBackendRequestEligibleForReauth(request.url)
  );
}

function isBackendRequestEligibleForReauth(url: string): boolean {
  const path = toPath(url);
  return path !== authEndpointsPrefix && !path.startsWith(`${authEndpointsPrefix}/`);
}

function toPath(url: string): string {
  try {
    return new URL(url, window.location.origin).pathname;
  } catch {
    return url;
  }
}

function redirectToLogin(router: Router): void {
  if (router.url === appRouteUrls.login || redirectInFlight) {
    return;
  }

  redirectInFlight = router.navigateByUrl(appRouteUrls.login).finally(() => {
    redirectInFlight = null;
  });
}
