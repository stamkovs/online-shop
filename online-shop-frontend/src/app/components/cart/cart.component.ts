import {ProductDetails} from '../../models/ProductDetails';
import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CartService} from '../../services/cart.service';
import {ActivatedRoute, Router} from '@angular/router';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'shoptastic-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CartComponent implements OnInit {

  products: ProductDetails[];
  totalAmount: any = 0.00;

  constructor(private cartService: CartService, private router: Router, private route: ActivatedRoute,
              private _snackBar: MatSnackBar) {
  }

  ngOnInit(): void {
    this.products = JSON.parse(this.cartService.getProductsFromCart()) || [];
    if (this.products) {
      this.products.forEach(product => {
        this.totalAmount += product.price
      });
      this.totalAmount = this.totalAmount.toFixed(2);
    }
  }

  deleteItem(item) {
    item.isAddedToCart = false;
    this.cartService.removeProductFromCart(item.id);
    this.products = JSON.parse(this.cartService.getProductsFromCart());
    this.totalAmount -= item.price;
    this.totalAmount = this.totalAmount.toFixed(2);
  }

  goToProductDetail(item) {
    this.router.navigate(['/products', item.category, item.id], {
      relativeTo: this.route, state: {id: true, data: item},
      queryParams: {navigatingThroughCategory: true}
    });
  }

  checkout() {
    this.openSnackBar();
    localStorage.clear();
    this.products = [];
    this.totalAmount = 0.00;
  }

  openSnackBar() {
    this._snackBar.open('Order completed successfully.', 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
