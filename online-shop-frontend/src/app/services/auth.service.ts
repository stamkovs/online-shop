import {Injectable} from '@angular/core';
import {WindowService} from './window.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {UserLoginDto} from '../models/UserLoginDto';
import {EmailDto} from '../models/EmailDto';

@Injectable()
export class AuthService {

  private readonly POPUP_WINDOW_HTML_CONTENT: string = '<html lang="en">' +
    '<head>' +
    '<title>OAuth2 Login</title>' +
    '<style>' +
    'p {font-weight: bold;}' +
    '.loader {' +
    'margin: 0 auto;' +
    '  border: 16px solid #f3f3f3; \n' +
    '  border-top: 16px solid #3498db; \n' +
    '  border-radius: 50%;\n' +
    '  width: 120px;\n' +
    '  height: 120px;\n' +
    '  animation: spin 2s linear infinite;\n' +
    '}\n' +
    '\n' +
    '@keyframes spin {\n' +
    '  0% { transform: rotate(0deg); }\n' +
    '  100% { transform: rotate(360deg); }\n' +
    '}' +
    '</style>' +
    '</head>' +
    '<body>' +
    '<p>One moment please..</p>' +
    '<div class="loader"></div>' +
    '</body>';

  private readonly oAuthCallbackUrl: string;
  private windowHandle: any = window;
  private intervalId: any = null;

  constructor(private windows: WindowService, private http: HttpClient) {
    this.oAuthCallbackUrl = 'https://shop.stamkov.com/home'
  }

  isLoggedIn(): Observable<any> {
    return this.http.get('auth/isLoggedIn');
  }

  login(userLoginDto: UserLoginDto): Observable<any> {
    return this.http.post('auth/login', userLoginDto);
  }

  logout(): Observable<any> {
    return this.http.get('auth/logout');
  }

  retrieveOauthUrls(): Observable<any> {
    return this.http.get('/auth/login/oauthEndpoints');
  }

  public OAuthLogin(oAuthUrl) {
    const windowName = oAuthUrl.includes('facebook') ? 'Facebook OAuth2 Login' : 'Google OAuth2 Login';
    this.windowHandle = this.windows.createWindow('oAuthUrl', windowName);

    this.windowHandle.document.write(this.POPUP_WINDOW_HTML_CONTENT);
    this.windowHandle.document.style = 'color: red;';
    setTimeout(() => {
      this.windowHandle = this.windows.createWindow(oAuthUrl, windowName);
    }, 600);

    this.checkForOAuthResponse();
    window.focus();
  }

  checkForOAuthResponse() {
    let loopCount = 600;
    let intervalLength = 200;
    this.intervalId = setInterval(() => {
      if (loopCount-- < 0) {
        clearInterval(this.intervalId);
        this.windowHandle.close();
      } else {
        let href = '';
        try {
          href = this.windowHandle.location.href;
        } catch (e) {
        }
        if (href != null) {
          let re = 'home';
          let found = href.match(re);
          if (found) {
            clearInterval(this.intervalId);
            this.windowHandle.close();
            if (href.includes('#_=_')) {
              href = href.replace('#_=_', '');
            }
            window.location.href = href;
          } else {
            if (href.indexOf(this.oAuthCallbackUrl) == 0) {
              clearInterval(this.intervalId);
              this.windowHandle.close();
            }
          }
        }
      }
    }, intervalLength);
  }

  forgotPassword(emailDto: EmailDto): Observable<any> {
    return this.http.post('auth/forgot-password', emailDto);
  }

}
