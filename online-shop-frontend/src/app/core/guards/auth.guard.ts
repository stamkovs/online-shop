import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {CookieService} from 'ngx-cookie';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private cookieService: CookieService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (next.routeConfig.path === 'entry') {
      const authCookie = this.cookieService.get("is_user_logged_in");
      if (authCookie) {
        this.router.navigate(['/home']);
        return false;
      }
    }

    if (next.routeConfig.path === 'products/add') {
      const authAdminCookie = this.cookieService.get("is_user_admin");
      if (!authAdminCookie) {
        this.router.navigate(['/home']);
        return false;
      }
    }
    return true;
  }
}
