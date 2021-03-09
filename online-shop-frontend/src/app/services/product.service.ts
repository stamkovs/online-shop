import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable()
export class ProductService {

  constructor(private http: HttpClient) {
  }

  getProducts(): Observable<any> {
    return this.http.get('rest/products');
  }

  getProductsByCategory(category: string): Observable<any> {
    return this.http.get(`rest/products/${category}`);
  }

  getProductById(id: number): Observable<any> {
    return this.http.get(`rest/product/${id}`)
  }

  getNewestProducts(): Observable<any> {
    return this.http.get('rest/products/newest');
  }

  searchProduct(searchValue: string): Observable<any> {
    const params = new HttpParams().set('searchValue', searchValue);
    return this.http.get('rest/products/search', {params: params});
  }
}
