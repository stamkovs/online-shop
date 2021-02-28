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
  breadcrumbsList: any;
  breadcrumbs: { url: string, label: string }[];

  constructor(private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit() {
    this.route.data.subscribe((data: any) => {
      this.products = [];
      this.breadcrumbs = [];
      this.showNoProductsMessage = false;
      const productCategory = this.route.snapshot.paramMap.get('productCategory');

      this.breadcrumbsList = location.pathname.split('/');
      if (this.breadcrumbsList.length > 2) {
        for (let i = 1; i < this.breadcrumbsList.length; i++) {
          const link = '/' + this.breadcrumbsList[i - 1] + '/' + this.breadcrumbsList[i]
          const breadcrumb = {"url": link, "label": this.breadcrumbsList[i]};
          this.breadcrumbs.push(breadcrumb);
        }
      }

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
