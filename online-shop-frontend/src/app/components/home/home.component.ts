import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import Glide from '@glidejs/glide';

import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../models/ProductDetails';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  products: ProductDetails[];

  constructor(private router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {

    this.route.data.subscribe((data: any) => {
      this.products = data.newestProducts;
    });

    setTimeout(() => {
      const glide = new Glide('.glide', {
        type: 'carousel',
        perView: 3,
        autoplay: 3000,
        hoverpause: true,
        breakpoints: {
          768: {
            perView: 1
          },
          1024: {perView: 2},
          1200: {perView: 3},
        }
      });
      glide.mount();

    }, 0);

  }

  goToProductDetail(item) {
    this.router.navigate(['/products', item.category, item.id], {
      relativeTo: this.route, state: {id: true, data: item},
      queryParams: {navigatingThroughCategory: true}
    });
  }

  addItemToWishList(event) {
    const productId = event.currentTarget.id;
  }
}
