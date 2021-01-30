import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class ConfirmAccountService {

  constructor(private http: HttpClient) {
  }

  getUserDetailsByTokenId(confirmationToken: string): Observable<any> {
    const params = new HttpParams().set('confirmationToken', confirmationToken);
    return this.http.get('auth/confirm-account', {params: params});
  }
}
