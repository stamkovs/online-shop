import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  googleLogo: string = '';
  fbLogo: string = '';
  googleAuthUrl: string = '';
  fbAuthUrl: string = '';

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.googleLogo = "../../assets/images/google-logo.png";
    this.fbLogo = "../../assets/images/fb-logo.png";
    // ToDo: If time available adapt the below code in angular way perhaps via shared service and observables.
    const signUpButton = document.getElementById('signUp');
    const signUpButtonMobile = document.getElementById('signUp-mobile');
    const signInButton = document.getElementById('signIn');
    const signInButtonMobile = document.getElementById('signIn-mobile');
    const container = document.getElementById('login-container');

    signUpButton.addEventListener('click', () =>
      container.classList.add('right-panel-active'));

    signUpButtonMobile.addEventListener('click', () =>
      container.classList.add('right-panel-active'));

    signInButton.addEventListener('click', () =>
      container.classList.remove('right-panel-active'));

    signInButtonMobile.addEventListener('click', () =>
      container.classList.remove('right-panel-active'));

    this.authService.retrieveOauthUrls().subscribe((data: any) => {
      this.googleAuthUrl = data.google;
      this.fbAuthUrl = data.facebook;
    })
  }

  doLogin(e) {
    let oAuthUrl: string;
    if (e.target.classList.contains("facebook")) {
      oAuthUrl = this.fbAuthUrl;
    } else {
      oAuthUrl = this.googleAuthUrl;
    }

    this.authService.OAuthLogin(oAuthUrl);
  }

}
