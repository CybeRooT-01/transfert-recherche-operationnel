import { CanActivateFn } from '@angular/router';
import { Router } from '@angular/router';
import { inject } from '@angular/core';

export const guestGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const token = localStorage.getItem('token');
  const loginInProgress = localStorage.getItem('loginInProgress');

  if (!token || loginInProgress === 'true') {
    return true;
  }

  router.navigate(['/home']);
  return false;
};
