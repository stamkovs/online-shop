import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';
import {ProductDetails} from 'src/app/models/ProductDetails';
import {ProductService} from '../../services/product.service';
import {Observable} from 'rxjs';
import {WishlistService} from '../../services/wishlist.service';

@Injectable()
export class WishlistResolver implements Resolve<ProductDetails> {

  constructor(private wishlistService: WishlistService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ProductDetails> | ProductDetails {

    return this.wishlistService.getWishListProductsForLoggedInUser();
  }
}
