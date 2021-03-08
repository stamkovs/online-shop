import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {ProductDetails} from 'src/app/models/ProductDetails';
import {ProductService} from '../../services/product.service';
import {Observable} from 'rxjs';

@Injectable()
export class ProductDetailResolver implements Resolve<ProductDetails> {

  constructor(private productService: ProductService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ProductDetails> | ProductDetails {

    if (route.queryParams.navigatingThroughCategory === true) {
      return;
    }
    const productId = +route.params.id;
    return this.productService.getProductById(productId);
  }
}
