import {Component, OnInit} from '@angular/core';
import {ContactSupportMailDto} from '../../models/ContactSupportMailDto';
import {ContactService} from '../../services/contact.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {

  userName: string = '';
  userEmail: string = '';
  userMessage: string = '';
  nameValid: boolean = false;
  emailValid: boolean = false;
  messageValid: boolean = false;
  disableSendBtn: boolean = true;

  constructor(private contactService: ContactService, private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
  }

  onNameChange(event) {
    this.userName = event;
    this.nameValid = this.userName !== '';
    this.checkIfAllFieldsAreValid();
  }

  onEmailChange(event) {
    this.userEmail = event;
    const regularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    this.emailValid = regularExpression.test(String(this.userEmail).toLowerCase());
    this.checkIfAllFieldsAreValid();
  }

  onMessageChange(event) {
    this.userMessage = event;
    this.messageValid = this.userMessage !== '';
    this.checkIfAllFieldsAreValid();
  }

  checkIfAllFieldsAreValid() {
    this.disableSendBtn = !(this.emailValid && this.nameValid && this.messageValid);
  }

  sendMailSupport() {
    let contactSupportMailDto = new ContactSupportMailDto();
    contactSupportMailDto.name = this.userName;
    contactSupportMailDto.email = this.userEmail;
    contactSupportMailDto.message = this.userMessage;

    this.contactService.sendContactSupportEmail(contactSupportMailDto).subscribe((data: any) => {
      let message = 'Your message was successfully sent to contact support.';
      this.openSnackBar(message);
      this.userName = '';
      this.userEmail = '';
      this.userMessage = '';
      this.disableSendBtn = true;
    }, error => {
      console.log(error);
      let message = 'Failed to send message to contact support.';
      this.openSnackBar(message);
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
