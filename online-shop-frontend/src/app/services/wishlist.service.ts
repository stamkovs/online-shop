import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class WishlistService {

  constructor(private http: HttpClient) {
  }

  getWishListProductsForLoggedInUser(): Observable<any> {
    return this.http.get('rest/get-wishlist');
  }

  addProductToWishList(productId: string): Observable<any> {
    return this.http.put('rest/add-to-wishlist', productId);
  }

  deleteProductFromWishlistById(productId: string): Observable<any> {
    return this.http.delete(`rest/delete-from-wishlist/${productId}`);
  }

  deleteAllProductsFromWishlistBy(): Observable<any> {
    return this.http.delete('rest/delete-wishlist-for-user');
  }
}
