import {Component, Input, OnInit} from '@angular/core';
import {CookieService} from 'ngx-cookie';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../../models/ProductDetails';

@Component({
  selector: 'shoptastic-product-card',
  templateUrl: './product-card.component.html',
  styleUrls: ['./product-card.component.scss']
})
export class ProductCardComponent implements OnInit {

  productId: number;
  name: string;
  imageSrc: string;
  price: number;

  @Input()
  item: ProductDetails;

  constructor(private cookieService: CookieService, private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.name = this.item.name;
    this.imageSrc = this.item.imageSrc;
    this.price = this.item.price;
    this.item.category = this.item.category.replace('_', '-');
  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  goToProductDetail(item) {
    if (this.route.snapshot.params.productCategory) {
      return this.router.navigate([item.id], {
        relativeTo: this.route,
        state: {id: true, data: item},
        queryParams: {navigatingThroughCategory: true}
      });
    }
    this.router.navigate([item.category, item.id], {
      relativeTo: this.route, state: {id: true, data: item},
      queryParams: {navigatingThroughCategory: true}
    });
  }

  addItemToWishList(event) {
    const productId = event.currentTarget.id;
  }
}
