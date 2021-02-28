import {Component, Input, OnInit} from '@angular/core';
import {ProductCardDto} from '../../../models/ProductCardDto';
import {CookieService} from 'ngx-cookie';

@Component({
  selector: 'shoptastic-product-item',
  templateUrl: './product-item.component.html',
  styleUrls: ['./product-item.component.scss']
})
export class ProductItemComponent implements OnInit {

  productId: number;
  name: string;
  imageSrc: string;
  price: string;

  @Input()
  item: ProductCardDto;

  constructor(private cookieService: CookieService) {
  }

  ngOnInit(): void {
    this.productId = this.item.id;
    this.name = this.item.name;
    this.imageSrc = this.item.imageSrc;
    this.price = this.item.price;
  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  addItemToWishList(event) {
    const productId = event.currentTarget.id;
  }
}
