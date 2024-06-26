import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  Router,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from '../service/auth.service';

@Injectable({
  providedIn: 'root',
})
export class OrganizerPagesGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.isLoggedIn() && this.authService.getRole() !== 'ORGANIZER') {
      if (this.authService.getRole() === 'VISITOR') {
        this.router.navigate(['available-events']);
        return false;
      }
    } else if (!this.authService.isLoggedIn()) {
      this.router.navigate(['']);
      return false;
    }
    return true;
  }
}
