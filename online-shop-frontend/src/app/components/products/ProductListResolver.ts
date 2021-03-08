import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import { ProductDetails } from 'src/app/models/ProductDetails';
import {ProductService} from '../../services/product.service';
import {Observable} from 'rxjs';

@Injectable()
export class ProductListResolver implements Resolve<ProductDetails> {

  constructor(private productService: ProductService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ProductDetails> | ProductDetails {
    if (state.url === '/home') {
      return this.productService.getNewestProducts();
    }
    if (state.url === '/products') {
      return this.productService.getProducts();
    } else {
      const category = route.url[1].toString();
      return this.productService.getProductsByCategory(category);
    }
  }
}
