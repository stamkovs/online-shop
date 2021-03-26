import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../models/ProductDetails';
import {CartService} from '../../services/cart.service';
import {MatSnackBar} from '@angular/material/snack-bar';
import {WishlistService} from '../../services/wishlist.service';

@Component({
  selector: 'shoptastic-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class WishlistComponent implements OnInit {

  products: ProductDetails[];
  showNoProductsMessage: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private cartService: CartService,
              private _snackBar: MatSnackBar, private wishlistService: WishlistService) {
  }

  ngOnInit(): void {
    this.route.data.subscribe((data: any) => {
      this.products = [];
      if (data.wishlistProducts.length) {
        this.products = data.wishlistProducts;
      } else {
        this.showNoProductsMessage = true;
      }
    }, () => {
      this.router.navigate(['/home']);
    });
  }

  goToProductDetail(item) {
    this.router.navigate(['/products', item.category, item.id], {
      relativeTo: this.route, state: {id: true, data: item},
      queryParams: {navigatingThroughCategory: true}
    });
  }

  addItemToCart(product: ProductDetails) {
    if (this.cartService.checkIsProductInCartById(product.id)) {
      this.router.navigate(['/cart']);
    } else {
      this.cartService.addProductToCart(product);
      this.openSnackBar(product.name);
    }
  }

  removeProductFromWishlist(productId: number) {
    this.wishlistService.deleteProductFromWishlistById('' + productId).subscribe(() => {
      const productIndex = this.products.findIndex(product => product.id === productId);
      if (productIndex > -1) {
        this.products.splice(productIndex, 1);
      }
      if (!this.products.length) {
        this.showNoProductsMessage = true;
      }
    }, () => {
      this.router.navigate(['/home']);
    });

  }

  removeAllProductsFromWishlist() {
    this.wishlistService.deleteAllProductsFromWishlistBy().subscribe(() => {
      this.products = [];
      this.showNoProductsMessage = true;
    });
  }

  openSnackBar(productName: string) {
    this._snackBar.open(productName + ' was added to cart.', 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }

}
