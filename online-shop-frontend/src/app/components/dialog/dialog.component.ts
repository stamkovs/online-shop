import {Component, EventEmitter, Inject, OnInit, ViewEncapsulation} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DialogComponent implements OnInit {

  category: string;
  description:string;
  longDescription:string;
  userInput: string;
  placeholder: string;
  userInputValid: boolean = false;
  buttonDisabled: boolean = true;
  userInputValidationMessage: string;
  buttonText: string;

  onSubmit = new EventEmitter();

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<DialogComponent>,
    @Inject(MAT_DIALOG_DATA) data) {

    this.category = data.category;
    this.description = data.description;
    this.longDescription = data.longDescription;
    this.placeholder = data.placeholder;
    this.buttonText = data.submitButtonText;
  }

  ngOnInit() {

    this.dialogRef.keydownEvents().subscribe(event => {
      if (event.key === "Escape") {
        this.close();
      }
    });

    this.dialogRef.backdropClick().subscribe(event => {
      this.close();
    });
  }

  submit() {
    this.onSubmit.emit(this.userInput);
  }

  close() {
    this.dialogRef.close();
  }

  validateInput(event) {
    if (this.category === 'forgot_password') {
      this.userInput = event;
      const regularExpression = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      this.userInputValid = regularExpression.test(String(event).toLowerCase());
      this.buttonDisabled = !this.userInputValid;
      this.userInputValidationMessage = 'Please provide a valid email address.';
    }
  }

}
