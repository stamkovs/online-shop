import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {EntryService} from '../../services/entry.service';
import {UserRegisterDto} from '../../models/UserRegisterDto';

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.scss']
})
export class EntryComponent implements OnInit {

  userRegister: UserRegisterDto = new UserRegisterDto();

  googleLogo: string = '';
  fbLogo: string = '';
  googleAuthUrl: string = '';
  fbAuthUrl: string = '';
  userFirstName: string = '';
  userLastName: string = '';
  userEmail: string = '';
  password: string = '';
  confirmPassword: string = '';
  disabled: boolean = true;
  passwordMismatch: boolean = null;
  showVerifyEmailMessage: boolean = false;
  verifyEmailMessage: string = '';
  firstNameValid: boolean = false;
  lastNameValid: boolean = false;
  emailValid: boolean = false;
  strength: number = 0;
  passwordStrengthText: string = '';
  textColor: string = '';

  constructor(private authService: AuthService, private entryService: EntryService) {
  }

  ngOnInit(): void {
    this.googleLogo = '../../assets/images/google-logo.png';
    this.fbLogo = '../../assets/images/fb-logo.png';
    // ToDo: If time available adapt the below code in angular way perhaps via shared service and observables.
    const signUpButton = document.getElementById('signUp');
    const signUpButtonMobile = document.getElementById('signUp-mobile');
    const signInButton = document.getElementById('signIn');
    const signInButtonMobile = document.getElementById('signIn-mobile');
    const container = document.getElementById('entry-container');

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

  loginWithGoogle() {
    this.authService.OAuthLogin(this.googleAuthUrl);
  }

  loginWithFacebook() {
    this.authService.OAuthLogin(this.fbAuthUrl);
  }

  registerUser() {
    this.userRegister.firstName = this.userFirstName;
    this.userRegister.lastName = this.userLastName;
    this.userRegister.email = this.userEmail;
    this.userRegister.password = this.password;
    this.userRegister.confirmPassword = this.confirmPassword;
    this.disabled = true;
    this.resetInputFields();
    let registerMessage = 'Verification email has been sent to ' + this.userRegister.email + '.';
    this.sendEmail(registerMessage);
  }

  resendEmail() {
    let resendVerificationEmailMessage = 'New verification email has been sent to ' + this.userRegister.email + '.';
    this.sendEmail(resendVerificationEmailMessage)
  }

  sendEmail(message: string) {
    this.entryService.registerUser(this.userRegister).subscribe(() => {
      this.showVerifyEmailMessage = true;
      this.verifyEmailMessage = message;
    }, error => {
      this.showVerifyEmailMessage = false;
      this.verifyEmailMessage = 'An error occured. ' + error;
    });
  }

  resetInputFields() {
    this.userFirstName = '';
    this.firstNameValid = false;
    this.userLastName = '';
    this.lastNameValid = false;
    this.userEmail = '';
    this.emailValid = false;
    this.password = '';
    this.confirmPassword = '';
  }

  firstNameModelChange(event) {
    this.userFirstName = event;
    this.firstNameValid = this.userFirstName !== '';
    this.checkIfAllFieldsAreValid();
  }

  lastNameModelChange(event) {
    this.userLastName = event;
    this.lastNameValid = this.userLastName !== '';
    this.checkIfAllFieldsAreValid();
  }

  emailModelChange(event) {
    this.userEmail = event;
    const regularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    this.emailValid = regularExpression.test(String(event).toLowerCase());
    this.disabled = !this.emailValid;
    this.checkIfAllFieldsAreValid();
  }

  passwordModelChange(event) {
    this.password = event;
    if (this.confirmPassword === '' || this.confirmPassword === event) {
      this.disabled = false;
      this.passwordMismatch = false;
    } else {
      this.disabled = true;
      this.passwordMismatch = true;
    }
    this.checkIfAllFieldsAreValid();
    this.strength = 0;
    this.passwordCheck(this.password);
  }

  passwordCheck(password) {
    if (password.length >= 8)
      this.strength += 1;

    if (password.match(/(?=.*[0-9])/))
      this.strength += 1;

    if (password.match(/(?=.*[!,%,&,@,#,$,^,*,?,_,~,<,>,])/))
      this.strength += 1;

    if (password.match(/(?=.*[a-z])/))
      this.strength += 1;

    if (password.match(/(?=.*[A-Z])/))
      this.strength += 1;

    this.displayBar(this.strength);
  }

  displayBar(strength) {
    switch (strength) {
      case 1:
        document.getElementById("password-strength-bar").setAttribute("style",
          "width: 20%; background: #de1616");
        this.passwordStrengthText = 'Very weak';
        this.textColor = 'red';
        break;

      case 2:
        document.getElementById("password-strength-bar").setAttribute("style",
          "width: 40%; background: #de1616");
        this.passwordStrengthText = 'Weak';
        this.textColor = 'red';
        break;

      case 3:
        document.getElementById("password-strength-bar").setAttribute("style",
          "width: 60%; background: #de1616");
        this.passwordStrengthText = 'Medium';
        this.textColor = 'red';
        break;

      case 4:
        document.getElementById("password-strength-bar").setAttribute("style",
          "width: 80%; background: #FFA200");
        this.passwordStrengthText = 'Strong';
        this.textColor = 'orange';
        break;

      case 5:
        document.getElementById("password-strength-bar").setAttribute("style",
          "width: 100%; background: #32CD32");
        this.passwordStrengthText = 'Very strong';
        this.textColor = 'green';
        break;

      default:
        document.getElementById("password-strength-bar").setAttribute("style",
          "width: 0; background: #de1616");
        this.passwordStrengthText = '';
        this.textColor = 'red';
    }
  }

  focusPasswordField() {
    document.getElementById("password-strength-wrapper").setAttribute("style",
      "height: 28px;");
  }

  onBlur() {
    document.getElementById("password-strength-wrapper").setAttribute("style",
      "height: 0");
  }

  confirmPasswordModelChange(event) {
    this.confirmPassword = event;
    if (this.password === event) {
      this.disabled = false;
      this.passwordMismatch = false;
    } else {
      this.disabled = true;
      this.passwordMismatch = true;
    }
    this.checkIfAllFieldsAreValid();
  }

  checkIfAllFieldsAreValid() {
    this.verifyEmailMessage = '';
    this.showVerifyEmailMessage = false;
    this.disabled = !(this.firstNameValid && this.lastNameValid && this.emailValid &&
      this.passwordMismatch === false && this.password !== '' && this.confirmPassword !== '');
  }

}
