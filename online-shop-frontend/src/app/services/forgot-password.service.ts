import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ResetPasswordDto} from '../models/ResetPasswordDto';

@Injectable()
export class ForgotPasswordService {

  constructor(private http: HttpClient) {
  }

  getUserDetailsByTokenId(resetPasswordToken: string): Observable<any> {
    const params = new HttpParams().set('resetPasswordToken', resetPasswordToken);
    return this.http.get('auth/check-reset-password-token-validity', {params: params});
  }

  updatePassword(resetPasswordDto: ResetPasswordDto, resetPasswordToken: string): Observable<any> {
    const params = new HttpParams().set('resetPasswordToken', resetPasswordToken);
    return this.http.put('auth/update-password', resetPasswordDto, {params: params});
  }
}
