import {Component, OnInit} from '@angular/core';
import {ConfirmAccountService} from '../../../services/confirm-account.service';
import {UserRegisterDto} from '../../../models/UserRegisterDto';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'confirm-account',
  templateUrl: './confirm-account.component.html',
  styleUrls: ['./confirm-account.component.scss']
})
export class ConfirmAccountComponent implements OnInit {

  isConfirmed: boolean = true;
  userRegister: UserRegisterDto = new UserRegisterDto();
  token: string;

  constructor(private completeAccountService: ConfirmAccountService, private route: ActivatedRoute,
              private router: Router) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });

    this.completeAccountService.getUserDetailsByTokenId(this.token).subscribe((user: UserRegisterDto) => {
      this.userRegister.firstName = user.firstName;
      this.userRegister.lastName = user.lastName;
      this.userRegister.email = user.email;
      this.isConfirmed = true;
      setTimeout(() => {
        this.router.navigate(['/home']);
      }, 1000);
    }, error => {
      this.isConfirmed = false;
      this.router.navigate(['/login']);
    });
  }

}
