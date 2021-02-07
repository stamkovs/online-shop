import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ResetPasswordDto} from '../../models/ResetPasswordDto';
import {ForgotPasswordService} from '../../services/forgot-password.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'confirm-account',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {

  isConfirmed: boolean = true;
  resetPasswordDto: ResetPasswordDto = new ResetPasswordDto();
  token: string;
  password: string = '';
  confirmPassword: string = '';
  passwordMismatch: boolean = null;
  updatePasswordDisabled: boolean = true;

  constructor(private route: ActivatedRoute, private router: Router,
              private forgotPasswordService: ForgotPasswordService,
              private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });

    this.forgotPasswordService.getUserDetailsByTokenId(this.token).subscribe((user: ResetPasswordDto) => {
      this.resetPasswordDto.email = user.email;
    }, error => {
      this.router.navigate(['/login']);
    });
  }

  updatePassword() {
    this.resetPasswordDto.newPassword = this.password;
    this.forgotPasswordService.updatePassword(this.resetPasswordDto, this.token).subscribe(data => {
      this.openSnackBar();
      this.router.navigate(['/home'])
    }, error => {
      this.router.navigate(['/login']);
    });
  }

  passwordModelChange(event) {
    this.password = event;
    if (this.confirmPassword === event) {
      this.updatePasswordDisabled = false;
      this.passwordMismatch = false;
    } else {
      this.updatePasswordDisabled = true;
      this.passwordMismatch = true;
    }
  }

  confirmPasswordModelChange(event) {
    this.confirmPassword = event;
    if (this.password === event) {
      this.updatePasswordDisabled = false;
      this.passwordMismatch = false;
    } else {
      this.updatePasswordDisabled = true;
      this.passwordMismatch = true;
    }
  }

  openSnackBar() {
    this._snackBar.open('Password successfully updated. You are now logged in.', 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
