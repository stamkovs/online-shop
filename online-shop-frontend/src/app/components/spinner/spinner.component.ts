import {Component, Input, OnInit} from '@angular/core';
import {SpinnerService} from '../../services/SpinnerService';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'shoptastic-spinner',
  templateUrl: './spinner.component.html',
  styleUrls: ['./spinner.component.scss']
})
export class SpinnerComponent implements OnInit {

  showSpinner: boolean;

  @Input() spinnerChange: BehaviorSubject<boolean>;

  constructor(private spinnerService: SpinnerService) {
    this.spinnerService.visibility.pipe().subscribe(visibility => {
      console.log(visibility);
      this.showSpinner = visibility;
    });
  }

  ngOnInit(): void {
  }

}
