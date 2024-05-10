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
export class VisitorPagesGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.isLoggedIn() && this.authService.getRole() !== 'VISITOR') {
      if (this.authService.isLoggedIn() && this.authService.getRole() === 'ORGANIZER') {
        this.router.navigate(['add-event']);
        return false;
      }
    } else if (!this.authService.isLoggedIn()) {
      this.router.navigate(['']);
      return false;
    }
    return true;
  }
}
