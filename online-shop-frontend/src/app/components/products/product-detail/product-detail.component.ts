import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ProductDetails} from '../../../models/ProductDetails';
import {ActivatedRoute, Router} from '@angular/router';
import {CookieService} from 'ngx-cookie';
import Drift from 'drift-zoom';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProductDetailComponent implements OnInit {

  item: ProductDetails;
  routeState: any;

  constructor(private router: Router, private route: ActivatedRoute, private cookieService: CookieService) {
    if (this.router.getCurrentNavigation().extras.state) {
      this.routeState = this.router.getCurrentNavigation().extras.state;
      if (this.routeState) {
        this.item = this.routeState.data;
      }
    }
    this.router.navigate([], {queryParams: null});
  }

  ngOnInit(): void {
    this.route.data.subscribe((data: any) => {
      if (data.productDetail) {
        this.item = data.productDetail;
      }
    });

    let imageZoomTrigger = document.querySelector('.product-image-zoom');
    let paneContainer = document.querySelector('.product-detail');

    let options = {
      paneContainer: paneContainer,
      inlinePane: false,
      zoomFactor: 2.5,
      // hoverDelay: 200,
    };
    new Drift(imageZoomTrigger, options);

  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  addItemToWishList(event) {
    const productId = event.currentTarget.id;
  }
}
