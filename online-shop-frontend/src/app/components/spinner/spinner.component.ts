import {Component, Input, OnInit} from '@angular/core';
import {SpinnerService} from '../../services/spinner.service';
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
      this.showSpinner = visibility;
    });
  }

  ngOnInit(): void {
  }

}
