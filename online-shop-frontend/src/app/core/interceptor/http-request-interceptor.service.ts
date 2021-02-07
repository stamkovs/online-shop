import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';


import {SpinnerService} from '../../services/SpinnerService';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

  constructor(private spinnerService: SpinnerService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    this.spinnerService.show();

    return next
      .handle(req)
      .pipe(
        tap((event: HttpEvent<any>) => {
          if (event instanceof HttpResponse) {
            setTimeout(() => {
              this.spinnerService.hide();
            }, 800);
          }
        }, (error) => {
          setTimeout(() => {
            this.spinnerService.hide();
          }, 800);
        })
      );
  }

}


