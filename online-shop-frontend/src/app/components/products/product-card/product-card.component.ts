import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CookieService} from 'ngx-cookie';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../../models/ProductDetails';
import {CartService} from '../../../services/cart.service';
import {WishlistService} from '../../../services/wishlist.service';

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
  title: string;

  @Input()
  item: ProductDetails;

  @Input()
  itemClickTitle: string;

  @Output() clickEvent = new EventEmitter<string>();
  @Output() addToCartEvent = new EventEmitter<string>();
  @Output() addToWishlistEvent = new EventEmitter<string>();

  constructor(private cookieService: CookieService, private router: Router, private route: ActivatedRoute,
              private cartService: CartService, private wishlistService: WishlistService) {
  }

  ngOnInit(): void {
    this.name = this.item.name;
    this.imageSrc = this.item.imageSrc;
    this.price = this.item.price;
    this.item.category = this.item.category.replace('_', '-');
    this.title = this.itemClickTitle;
  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  emitClickEvent(item) {
    this.clickEvent.emit(item);
  }

  emitAddToWishlistEvent(item) {
    this.addToWishlistEvent.emit(item);
    this.item.wishlisted = !this.item.wishlisted;
  }

  addItemToWishList(productId: number) {
    this.wishlistService.addProductToWishList('' + productId);
  }

}
