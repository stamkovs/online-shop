import { throwError as observableThrowError, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Injectable, Injector } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';


import { Router } from '@angular/router';
import {CookieService} from 'ngx-cookie';
import {AuthService} from '../../services/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private injector: Injector, private cookieService: CookieService, private authService: AuthService) {}

  public get router(): Router {
    return this.injector.get(Router);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

      return next.handle(req).pipe(
        catchError((error) => {
          if (error instanceof HttpErrorResponse) {
            if (error.status === 401) {
              this.redirectToEntryPage();
            } else if (error.status === 307) {
              this.redirectToLogout();
            } else if (error.status === 400 || error.status === 409 || error.status === 422) {
              return observableThrowError(error.error);
            } else {
              this.redirectToErrorPage();
            }
          }
          return of(error);
        }));
  }

  redirectToEntryPage() {
    this.cookieService.put('is_user_logged_in', '');
    this.router.navigate(['/login']);
  }

  redirectToLogout() {
    this.authService.logout();
  }

  redirectToErrorPage() {
    this.router.navigate(['/error']);
  }
}


