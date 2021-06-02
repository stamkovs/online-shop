import {Component, HostListener, OnInit, ViewEncapsulation} from '@angular/core';
import Glide from '@glidejs/glide';

import {ActivatedRoute, Router} from '@angular/router';
import {ProductDetails} from '../../models/ProductDetails';
import {CookieService} from 'ngx-cookie';
import {WishlistService} from '../../services/wishlist.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit {

  products: ProductDetails[];
  dragging: boolean = false;
  rightClick: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, private cookieService: CookieService,
              private wishlistService: WishlistService) {
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

  @HostListener('document:contextmenu', ['$event'])
  onDocumentRightClick(event) {
    console.log(event);
    this.rightClick = true;
  }

  mouseDownEvent(event) {
    switch (event.which) {
      // left click
      case 1:
        this.dragging = false;
        this.rightClick = false;
        break;
      // right click
      case 3:
        this.rightClick = true;
        break;
    }
  }

  goToProductDetail(item) {
    if (this.dragging || this.rightClick) {
      return;
    }
    item.category = item.category.replace('_', '-');
    this.router.navigate(['/products', item.category, item.id], {
      relativeTo: this.route, state: {id: true, data: item},
      // queryParams: {navigatingThroughCategory: true}
    });
  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  addOrGoToWishlist(product: ProductDetails, event) {
    if (product.wishlisted) {
      return this.goToWishlist();
    }
    event.target.classList.add('button-loading');
    this.wishlistService.addProductToWishList('' + product.id).subscribe(() => {
      setTimeout(() => {
        event.target.classList.remove('button-loading');
        product.wishlisted = true;
      }, 2000);
    });
  }

  goToWishlist() {
    this.router.navigate(['/wishlist']);
  }
}
