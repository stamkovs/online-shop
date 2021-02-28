import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

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


  constructor(private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.route.data.subscribe((data: any) => {
      this.products = [];
      this.showNoProductsMessage = false;
      const productCategory = this.route.snapshot.paramMap.get('productCategory');

      if (productCategory) {
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
      } else {
        this.pageTitle = 'Shoptastic Products';
      }
      if (data.productsData.products.length) {
        this.products = data.productsData.products;
      } else {
        this.showNoProductsMessage = true;
      }
    });
  }

}
