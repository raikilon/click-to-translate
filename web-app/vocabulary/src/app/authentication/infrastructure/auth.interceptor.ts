import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { from, switchMap } from 'rxjs';
import { AuthSessionService } from '../application/auth-session.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
  const authSessionService = inject(AuthSessionService);

  return from(authSessionService.getAccessToken()).pipe(
    switchMap((accessToken) => {
      if (!accessToken) {
        return next(request);
      }

      return next(
        request.clone({
          setHeaders: {
            Authorization: `Bearer ${accessToken}`
          }
        })
      );
    })
  );
};
