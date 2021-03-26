import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {EntryService} from '../../services/entry.service';
import {UserRegisterDto} from '../../models/UserRegisterDto';
import {UserLoginDto} from '../../models/UserLoginDto';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig} from '@angular/material/dialog';
import {DialogComponent} from '../dialog/dialog.component';
import {ResetPasswordDto} from '../../models/ResetPasswordDto';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-entry',
  templateUrl: './entry.component.html',
  styleUrls: ['./entry.component.scss']
})
export class EntryComponent implements OnInit {

  userRegister: UserRegisterDto = new UserRegisterDto();
  userLogin: UserLoginDto = new UserLoginDto();
  resetPasswordDto: ResetPasswordDto = new ResetPasswordDto();

  googleLogo: string = '';
  fbLogo: string = '';
  googleAuthUrl: string = '';
  fbAuthUrl: string = '';
  userFirstName: string = '';
  userLastName: string = '';
  userEmail: string = '';
  password: string = '';
  confirmPassword: string = '';
  signUpDisabled: boolean = true;
  signInDisabled: boolean = true;
  passwordMismatch: boolean = null;
  showVerifyEmailMessage: boolean = false;
  showEmailRegisterErrorMessage: boolean = false;
  verifyEmailMessage: string = '';
  firstNameValid: boolean = false;
  lastNameValid: boolean = false;
  emailValid: boolean = false;
  strength: number = 0;
  passwordStrengthText: string = '';
  textColor: string = '';
  showLoginErrorMessage: boolean = false;
  loginErrorMessageText: string = '';

  constructor(private authService: AuthService, private entryService: EntryService, private router: Router,
              private dialog: MatDialog, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.googleLogo = '../../assets/images/google-logo.png';
    this.fbLogo = '../../assets/images/fb-logo.png';
    const signUpButton = document.getElementById('signUp');
    const signUpButtonMobile = document.getElementById('signUp-mobile');
    const signInButton = document.getElementById('signIn');
    const signInButtonMobile = document.getElementById('signIn-mobile');
    const container = document.getElementById('entry-container');

    signUpButton.addEventListener('click', () => {
      container.classList.add('right-panel-active');
      this.resetInputFields();
      document.getElementById("password-strength-wrapper").setAttribute("style",
        "height: 0");
      document.getElementById("password-strength-bar").setAttribute("style",
        "width: 0; background: #de1616");
    });


    signUpButtonMobile.addEventListener('click', () => {
      container.classList.add('right-panel-active');
      this.resetInputFields();
      document.getElementById("password-strength-wrapper").setAttribute("style",
        "height: 0");
      document.getElementById("password-strength-bar").setAttribute("style",
        "width: 0; background: #de1616");
    });

    signInButton.addEventListener('click', () => {
      container.classList.remove('right-panel-active');
      this.resetInputFields();
    });


    signInButtonMobile.addEventListener('click', () => {
      container.classList.remove('right-panel-active');
      this.resetInputFields();
    });


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
    this.signUpDisabled = true;
    this.resetInputFields();
    let registerMessage = 'Verification email has been sent to ' + this.userRegister.email + '.';
    this.registerUserAccount(registerMessage);
  }

  resendEmail() {
    let resendVerificationEmailMessage = 'New verification email has been sent to ' + this.userRegister.email + '.';
    this.registerUserAccount(resendVerificationEmailMessage)
  }

  registerUserAccount(message: string) {
    this.entryService.registerUser(this.userRegister).subscribe(() => {
      this.showVerifyEmailMessage = true;
      this.showEmailRegisterErrorMessage = false;
      this.verifyEmailMessage = message;
    }, error => {
      this.showVerifyEmailMessage = false;
      this.showEmailRegisterErrorMessage = true;
      if (error) {
        this.verifyEmailMessage = error;
      } else {
        this.verifyEmailMessage = 'An unexpected error occured.';
      }
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
    this.showLoginErrorMessage = false;
    this.showVerifyEmailMessage = false;
    this.showEmailRegisterErrorMessage = false;
    this.passwordStrengthText = '';
    this.strength = 0;
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
    this.signUpDisabled = !this.emailValid;
    this.checkIfAllFieldsAreValid();
  }

  passwordModelChange(event) {
    this.showLoginErrorMessage = false;
    this.password = event;
    if (this.confirmPassword === '' || this.confirmPassword === event) {
      this.signUpDisabled = false;
      this.passwordMismatch = false;
    } else {
      this.signUpDisabled = true;
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
    this.showLoginErrorMessage = false;
  }

  emailFocusLogin() {
    this.showLoginErrorMessage = false;
  }

  onBlurPasswordStrength() {
    document.getElementById("password-strength-wrapper").setAttribute("style",
      "height: 0");
  }

  confirmPasswordModelChange(event) {
    this.confirmPassword = event;
    if (this.password === event) {
      this.signUpDisabled = false;
      this.passwordMismatch = false;
    } else {
      this.signUpDisabled = true;
      this.passwordMismatch = true;
    }
    this.checkIfAllFieldsAreValid();
  }

  checkIfAllFieldsAreValid() {
    this.verifyEmailMessage = '';
    this.showVerifyEmailMessage = false;
    this.signUpDisabled = !(this.firstNameValid && this.lastNameValid && this.emailValid &&
      this.passwordMismatch === false && this.password !== '' && this.confirmPassword !== '');
    this.signInDisabled = !(this.emailValid && this.password !== '');
  }

  loginUser() {
    this.userLogin.email = this.userEmail;
    this.userLogin.password = this.password;
    this.authService.login(this.userLogin).subscribe(data => {
      this.router.navigate(['/home']);
    }, error => {
      if (error != null && error === 'Invalid email or password.') {
        this.showLoginErrorMessage = true;
        this.loginErrorMessageText = 'Login failed. Email or password is incorrect.';
      }
    });
  }

  forgotPasswordDialog() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.width = '40%';
    // dialogConfig.height = '50%';
    const description = 'Forgot your password?';
    const longDescription = 'That\'s okay, it happens!\nPlease enter your email and click on the button below to reset' +
      ' your password.\n';
    const category = 'forgot_password';
    const placeholder = 'Your email';
    const submitButtonText = 'Reset Password';
    dialogConfig.data = {
      description, longDescription, category, placeholder, submitButtonText
    };

    const dialogRef = this.dialog.open(DialogComponent, dialogConfig);
    const resetPassword = dialogRef.componentInstance.onSubmit.subscribe(data => {
      this.resetPasswordDto.email = data;
      this.authService.forgotPassword(this.resetPasswordDto).subscribe(() => {
        const forgotPasswordSuccessMessage = 'Email for password reset was sent to ' + this.resetPasswordDto.email;
        this.openSnackBar(forgotPasswordSuccessMessage);
        dialogConfig.data.submitButtonText = 'Resend';
      }, error => {
        let forgotPasswordErrorMessage = '';
        if (error.includes('exist')) {
          forgotPasswordErrorMessage = error;
        } else {
          forgotPasswordErrorMessage = 'An unexpected error occurred. Please try again later.';
        }
        this.openSnackBar(forgotPasswordErrorMessage);
      });
    });

    dialogRef.afterClosed().subscribe(() => {
      dialogRef.componentInstance.onSubmit.unsubscribe();
    });
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }


}
