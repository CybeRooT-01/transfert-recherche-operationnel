import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  const token = localStorage.getItem('token');
  const loginInProgress = localStorage.getItem('loginInProgress');

  if (token && loginInProgress !== 'true') {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
