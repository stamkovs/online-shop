import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import Glide from '@glidejs/glide';

import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../models/ProductDetails';
import {CartService} from '../../services/cart.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  products: ProductDetails[];

  constructor(private router: Router, private route: ActivatedRoute, private cartService: CartService) {
  }

  ngOnInit(): void {
    this.route.data.subscribe((data: any) => {
      this.products = data.newestProducts;
    });

    setTimeout(() => {
      const glide = new Glide('.glide', {
        type: 'carousel',
        perView: 3,
        autoplay: 5000,
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

  addItemToCart(product: ProductDetails, event) {
    event.target.classList.add('button-loading');
    const btnInnerHTML = event.target.innerHTML;
    event.target.innerHTML = '';
    setTimeout(() => {
      event.target.classList.remove('button-loading');
      event.target.innerHTML = "<pre class='pre-button'>" + btnInnerHTML.replace('Add to cart', 'Go to cart') + "</pre>";
    }, 2000);
    this.cartService.addProductToCart(product);
  }

  goToCart() {
    this.router.navigate(['/cart']);
  }

  isItemInCart(productId: number) {
    return this.cartService.checkIsProductInCartById(productId);
  }
}
