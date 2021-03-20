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
  cartBtnLabel: string;
  sizes: any[];
  sizesAvailablePerProduct: any[];
  selectedSize: any;
  itemQuantity: number;
  cartInputElement: any;

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
        this.cartBtnLabel = this.isItemInCart(this.item.id) ? 'Go to cart' : 'Add to cart';
        this.itemQuantity = this.item.totalQuantity;
        if (this.item.category === 'men_sneakers') {
          this.sizes = ['36', '37', '38', '39', '40', '41', '42', '43', '44', '45'];
        }
        if (this.item.category === 'watches') {
          this.sizes = ['34', '36', '38', '40', '42', '44', '46'];
        }
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

    this.cartInputElement = document.getElementById('cart-counter-input');
  }

  checkIfProductHasSize(item: ProductDetails, size: string) {
    this.sizesAvailablePerProduct = Object.keys(item.sizeQuantityInfo.size);
    return !this.sizesAvailablePerProduct.includes(size);
  }

  toggleSelected(clickedSize) {
    if (this.cartBtnLabel === 'Choose size') {
      this.cartBtnLabel = 'Add to cart';
      document.getElementsByClassName('add-to-cart-btn')[0].classList.remove('warning-size-undefined');
    }
    this.itemQuantity = this.item.sizeQuantityInfo.size[clickedSize];
    this.selectedSize = clickedSize;
    this.cartInputElement.value = '1';
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
      this.wishlistService.addProductToWishList('' + productId).subscribe(() => {
        this.item.wishlisted = true;
      }, error => {
        console.log(error);
      });
    }
  }

  addProductToCart(product: ProductDetails, event) {
    if (this.selectedSize == undefined) {
      event.target.classList.add('warning-size-undefined');
      this.cartBtnLabel = 'Choose size';
      return;
    }
    event.target.classList.add('button-loading');
    setTimeout(() => {
      event.target.classList.remove('button-loading');
      this.openSnackBar(product.name, ' was successfully added to your cart.');
      this.cartBtnLabel = 'Go to cart';
    }, 2000);
    product.addedQuantityToCart = this.cartInputElement.value;
    product.addedSizeToCart = this.selectedSize;
    product.maximumQuantity = this.item.sizeQuantityInfo.size[this.selectedSize];
    this.itemQuantity -= this.cartInputElement.value;
    this.cartService.addProductToCart(product);
  }

  isItemInCart(productId: number) {
    return this.cartService.checkIsProductInCartById(productId);
  }

  decreaseCartCounter() {
    if (this.cartInputElement.value === '0') {
      return;
    }
    this.cartInputElement.value--;
  }

  increaseCartCounter() {
    if (this.cartInputElement.value === '' + this.itemQuantity) {
      return;
    }
    this.cartInputElement.value++;
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
    this.cartBtnLabel = 'Add to cart';
  }
}
