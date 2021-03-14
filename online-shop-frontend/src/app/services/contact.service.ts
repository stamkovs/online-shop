import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ContactSupportMailDto} from '../models/ContactSupportMailDto';

@Injectable()
export class ContactService {

  constructor(private http: HttpClient) {
  }

  sendContactSupportEmail(contactSupportMailDto: ContactSupportMailDto): Observable<any> {
    return this.http.post('rest/send-contact-support-mail', contactSupportMailDto);
  }
}
