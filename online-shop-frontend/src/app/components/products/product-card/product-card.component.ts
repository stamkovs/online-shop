import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CookieService} from 'ngx-cookie';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../../models/ProductDetails';
import {CartService} from '../../../services/cart.service';

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

  @Output() clickEvent = new EventEmitter<string>();
  @Output() addToCartEvent = new EventEmitter<string>();

  constructor(private cookieService: CookieService, private router: Router, private route: ActivatedRoute,
              private cartService: CartService) {
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

  emitClickEvent(item) {
    this.clickEvent.emit(item);
  }

  emitAddToCartEvent(item) {
    this.addToCartEvent.emit(item);
  }

  addItemToWishList(event) {
    const productId = event.currentTarget.id;
  }

  isItemInCart(productId: number) {
    return this.cartService.checkIsProductInCartById(productId);
  }
}
