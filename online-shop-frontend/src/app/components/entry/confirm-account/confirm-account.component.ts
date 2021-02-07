import {Component, OnInit} from '@angular/core';
import {ConfirmAccountService} from '../../../services/confirm-account.service';
import {UserRegisterDto} from '../../../models/UserRegisterDto';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['./confirm-account.component.scss']
})
export class ConfirmAccountComponent implements OnInit {

  isConfirmed: boolean = true;
  userRegister: UserRegisterDto = new UserRegisterDto();
  token: string;

  constructor(private confirmAccountService: ConfirmAccountService, private route: ActivatedRoute,
              private router: Router, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });

    this.confirmAccountService.getUserDetailsByTokenId(this.token).subscribe((user: UserRegisterDto) => {
      this.userRegister.email = user.email;
      this.isConfirmed = true;
      setTimeout(() => {
        this.openSnackBar();
        this.router.navigate(['/home']);
      }, 1000);

    }, error => {
      this.isConfirmed = false;
      this.router.navigate(['/login']);
    });
  }

  openSnackBar() {
    this._snackBar.open('Your account is verified. You are now logged in.', 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

}
