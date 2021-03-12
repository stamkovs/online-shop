import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subject} from 'rxjs';
import {ProductService} from '../../../services/product.service';
import {ProductDetails} from '../../../models/ProductDetails';
import {CartService} from '../../../services/cart.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProductListComponent implements OnInit {

  products: any[];
  pageTitle: string;
  showNoProductsMessage: boolean = false;
  componentInitialLoad: boolean = false;
  searchBar: any;

  categoryChangeSubject: Subject<void> = new Subject<void>();

  constructor(private route: ActivatedRoute, private router: Router, private productService: ProductService,
              private cartService: CartService, private _snackBar: MatSnackBar) {
  }

  ngOnInit() {
    this.route.data.subscribe((data: any) => {
      this.componentInitialLoad = true;
      this.initProductsList(data);
    });

    this.route.queryParams.subscribe((data: any) => {
      if (data.searchValue && this.componentInitialLoad === false) {
        this.productService.searchProduct(data.searchValue).subscribe((data: any) => {
          data.productsData = data;
          this.initProductsList(data);
        });
      } else {
        this.componentInitialLoad = false;
        if ((data.searchValue == undefined || data.searchValue === '') &&
          (this.router.url === '/products') || this.router.url === '/products?searchValue=') {
          this.productService.getProducts().subscribe((data: any) => {
            data.productsData = data;
            this.initProductsList(data);
            this.router.navigate([], {
              relativeTo: this.route,
              queryParams: {
                searchValue: null
              },
              queryParamsHandling: 'merge', // remove to replace all query params by provided
            });
            this.searchBar = document.getElementById('search-input');
            this.searchBar.value = '';
          });
        }
      }

    });
  }

  initProductsList(data: any) {
    this.products = [];
    this.showNoProductsMessage = false;
    const productCategory = this.route.snapshot.paramMap.get('productCategory');
    this.searchBar = document.getElementById('search-input');
    if (this.router.url.includes('searchValue=')) {
      this.searchBar.value = this.router.url.split('searchValue=')[1];
    }
    if (productCategory) {
      this.searchBar.value = '';
      switch (productCategory) {
        case 'men-sneakers':
          this.pageTitle = 'Men Sneakers Products';
          break;
        case 'women-sneakers':
          this.pageTitle = 'Women Sneakers Products';
          break;
        case 'sports':
          this.pageTitle = 'Sports Products';
          break;
        case 'electronics':
          this.pageTitle = 'Electronics Products';
          break;
        case 'watches':
          this.pageTitle = 'Watches Products';
          break;
        case 'jewelry':
          this.pageTitle = 'Jewelry Products';
          break;
        case 'products':
          this.pageTitle = 'Shoptastic Products'
          break;
        default:
          this.pageTitle = 'Category Not Found. Redirecting to main Product page';
          this.router.navigate(['/products']);
      }
      this.emitCategoryChange();

    } else {
      this.pageTitle = 'Shoptastic Products';
    }
    if (data.productsData.products) {
      this.products = data.productsData.products;
    } else if (data.productsData && data.productsData.length) {
      this.products = data.productsData;
    } else {
      this.showNoProductsMessage = true;
    }
  }

  emitCategoryChange() {
    this.categoryChangeSubject.next();
  }

  goToProductDetail(item) {
    if (this.route.snapshot.params.productCategory) {
      return this.router.navigate([item.id], {
        relativeTo: this.route,
        state: {id: true, data: item},
        queryParams: {navigatingThroughCategory: true}
      });
    }
    this.router.navigate([item.category, item.id], {
      relativeTo: this.route, state: {id: true, data: item}
    });
  }

  addItemToCart(product: ProductDetails) {
    if (this.cartService.checkIsProductInCartById(product.id)) {
      this.router.navigate(['/cart']);
    } else {
      this.cartService.addProductToCart(product);
      this.openSnackBar(product.name)
    }
  }

  openSnackBar(productName: string) {
    this._snackBar.open(productName + ' was added to cart.', 'Close', {
      duration: 5000,
      horizontalPosition: 'right',
      verticalPosition: 'top',
    });
  }
}
