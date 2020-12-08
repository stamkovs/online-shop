import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {

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
  }

}
