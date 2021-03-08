import {Component, Input, OnInit} from '@angular/core';
import {ProductDetails} from '../../models/ProductDetails';
import {Observable, Subscription} from 'rxjs';

@Component({
  selector: 'shoptastic-breadcrumbs',
  templateUrl: './breadcrumbs.component.html',
  styleUrls: ['./breadcrumbs.component.scss']
})
export class BreadcrumbsComponent implements OnInit {

  breadcrumbsList: any;
  breadcrumbs: { url: string, label: string }[];
  @Input() item: ProductDetails;

  private categoryChangeSubscription: Subscription;

  @Input() categoryChangeEvent: Observable<void>;

  constructor() {
  }

  ngOnInit(): void {
    this.initBreadcrumbs();
    if (this.categoryChangeEvent) {
      this.categoryChangeSubscription = this.categoryChangeEvent.subscribe(() => this.initBreadcrumbs());
    }
  }

  initBreadcrumbs() {
    this.breadcrumbs = [];

    this.breadcrumbsList = location.pathname.split('/');
    if (this.breadcrumbsList.length > 2) {
      let link = '/' + this.breadcrumbsList[1];
      this.breadcrumbsList[1] = this.breadcrumbsList[1].replace('-', ' ');
      const breadcrumb = {"url": link, "label": this.breadcrumbsList[1]};
      this.breadcrumbs.push(breadcrumb);
      for (let i = 2; i < this.breadcrumbsList.length; i++) {
        link += '/' + this.breadcrumbsList[i];
        this.breadcrumbsList[i] = this.breadcrumbsList[i].replace('-', ' ');
        const breadcrumb = {"url": link, "label": this.breadcrumbsList[i]};
        this.breadcrumbs.push(breadcrumb);
      }
      if (this.item) {
        this.breadcrumbs[this.breadcrumbs.length - 1].label = this.item.name;
      }
    }
  }

}
