import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserRegisterDto} from '../models/UserRegisterDto';
import {Observable} from 'rxjs';

@Injectable()
export class EntryService {

  constructor(private http: HttpClient) {
  }

  registerUser(registerUserDto: UserRegisterDto): Observable<any> {
    return this.http.post('/auth/register', registerUserDto);
  }
}
