import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ProductDetails} from '../../../models/ProductDetails';
import {ActivatedRoute, Router} from '@angular/router';
import {CookieService} from 'ngx-cookie';
import Drift from 'drift-zoom';
import {CartService} from '../../../services/cart.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {WishlistService} from '../../../services/wishlist.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProductDetailComponent implements OnInit {

  item: ProductDetails;
  routeState: any;
  wishlistBtnLabel: string;

  constructor(private router: Router, private route: ActivatedRoute, private cookieService: CookieService,
              private cartService: CartService, private _snackBar: MatSnackBar,
              private wishlistService: WishlistService) {

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
        this.wishlistBtnLabel = this.item.wishlisted ? 'Go to wishlist' : 'Wishlist';
      }
    });

    let imageZoomTrigger = document.querySelector('.product-image-zoom');
    let paneContainer = document.querySelector('.product-detail');

    let options = {
      paneContainer: paneContainer,
      inlinePane: false,
      zoomFactor: 2.5,
    };
    new Drift(imageZoomTrigger, options);

  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  addProductToWishlist(productId: number, event) {
    if (this.item.wishlisted) {
      this.router.navigate(['/wishlist']);
    } else {
      event.target.classList.add('button-loading');
      setTimeout(() => {
        event.target.classList.remove('button-loading');
        this.wishlistBtnLabel = 'Go to wishlist';
        this.openSnackBar(this.item.name, 'was successfully added to your wishlist.');
      }, 2000);
      this.wishlistService.addProductToWishList('' + productId).subscribe((data: any) => {
        this.item.wishlisted = true;
      }, error => {
      });
    }
  }

  addProductToCart(product: ProductDetails, event) {
    event.target.classList.add('button-loading');
    const btnInnerHTML = event.target.innerHTML;
    event.target.innerHTML = '';
    setTimeout(() => {
      event.target.classList.remove('button-loading');
      event.target.innerHTML = "<pre class='pre-button'>" + btnInnerHTML.replace('Add to cart', 'Go to cart') + "</pre>";
      this.openSnackBar(product.name, ' was successfully added to your cart.');
    }, 2000);
    this.cartService.addProductToCart(product);
  }

  isItemInCart(productId: number) {
    return this.cartService.checkIsProductInCartById(productId);
  }

  goToCart() {
    this.router.navigate(['/cart']);
  }

  openSnackBar(productName: string, message: string) {
    this._snackBar.open(productName + message, 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

  buyProduct(product: ProductDetails) {
    this.cartService.removeProductFromCart(product.id);
    this.openSnackBar(product.name, ' was successfully purchased.');
  }
}
