import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthenticationService} from '../services/authentication.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthenticationService);
  const router = inject(Router);

  const isAuthenticated = authService.isAuthenticated();

  if (true === isAuthenticated) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
