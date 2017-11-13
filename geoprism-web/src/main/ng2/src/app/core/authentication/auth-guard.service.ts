import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import { AuthService} from './auth.service';

declare var acp: any;

@Injectable()
export class AuthGuardService implements CanActivate {

  constructor(private service:AuthService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    
    if (this.service.isAdmin()) {
      return true; 
    }
    
    // Redirect to the login page
    document.location.href = acp + '/session/form';
    
    return false;
  }
}
