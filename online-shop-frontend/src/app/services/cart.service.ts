import {Injectable} from '@angular/core';
import {ProductDetails} from '../models/ProductDetails';

@Injectable()
export class CartService {

  products: ProductDetails[];

  constructor() {
  }

  private readonly cartItems = 'cartItems';

  getProductsFromCart() {
    return localStorage.getItem(this.cartItems);
  }

  addProductToCart(product: ProductDetails) {
    this.products = JSON.parse(localStorage.getItem(this.cartItems)) || [];
    let productExists = this.checkIsProductInCartById(product.id);

    if (!productExists) {
      this.products.push(product);
      return localStorage.setItem(this.cartItems, JSON.stringify(this.products));
    }
  }

  removeProductFromCart(productId: number) {
    this.products = JSON.parse(localStorage.getItem(this.cartItems));
    const productIndex = this.products.findIndex(product => product.id === productId);
    if (productIndex > -1) {
      this.products.splice(productIndex, 1);
      localStorage.setItem(this.cartItems, JSON.stringify(this.products));
    }
  }

  checkIsProductInCartById(productId: number) {
    let productExists = false;
    this.products = JSON.parse(localStorage.getItem(this.cartItems)) || [];
    if (this.products) {
      productExists = this.products.some(item => item.id === productId);
    }
    return productExists;
  }
}
