import {
  AfterViewInit,
  Component,
  HostListener,
  OnInit,
} from '@angular/core';
import {CookieService} from 'ngx-cookie';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit, AfterViewInit {
  navbar: HTMLElement;
  navbarToggle: HTMLElement;
  navbarMenu: HTMLElement;
  navbarLinksContainer: HTMLElement;
  searchInput: any;
  navbarItems: NodeListOf<HTMLElement>;
  productsSubMenuMarginSeparator: number;
  accountSubMenuMarginSeparator: number;
  screenHeight: number;
  screenWidth: number;
  loggedInUserEmail: string;
  loggedInUserFirstName: string;
  loggedInUserLastName: string;

  constructor(private cookieService: CookieService, private authService: AuthService,
              private router: Router) {
  }

  ngOnInit() {
    this.navbar = document.getElementById('navbar');
    this.navbarToggle = this.navbar.querySelector('.navbar-toggle');
    this.navbarMenu = this.navbar.querySelector('.navbar-menu');
    this.navbarLinksContainer = this.navbar.querySelector('.navbar-links');
    this.navbarItems = document.querySelectorAll('.navbar-item');
    this.productsSubMenuMarginSeparator = 0;
    this.accountSubMenuMarginSeparator = 0;
  }

  openMobileNavbar() {
    this.navbar.classList.add('opened');
    this.navbarToggle.setAttribute('aria-label', 'Close navigation menu.');
  }

  closeMobileNavbar() {
    if (this.screenWidth < 769 && this.navbar.classList.contains('opened')) {
      this.navbar.classList.remove('opened');
      this.navbarToggle.setAttribute('aria-label', 'Open navigation menu.');
    }
  }

  ngAfterViewInit() {
    this.navbarToggle.addEventListener('click', () => {
      if (this.navbar.classList.contains('opened')) {
        this.closeMobileNavbar();
      } else {
        this.openMobileNavbar();
      }
    });

    this.navbarLinksContainer.addEventListener('click', (clickEvent) => {
      clickEvent.stopPropagation();
    });

    this.navbarMenu.addEventListener('click', this.closeMobileNavbar);

    // added for IE11 since after page refresh it doesnt fire the window load event
    this.getScreenSizeOnLoad();
  }

  @HostListener('window:load', ['$event'])
  getScreenSizeOnLoad() {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  @HostListener('window:resize', ['$event'])
  getScreenSizeOnResize() {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  mouseEnterSubMenu(event) {
    if (this.screenWidth > 768) {
      this.productsSubMenuMarginSeparator = 0;
      this.accountSubMenuMarginSeparator = 0;
      return;
    }
    const clickedItem = event.target;
    const productsSubmenu = clickedItem.querySelector('.products-categories');
    const accountSubmenu = clickedItem.querySelector('.account-categories');
    if (productsSubmenu) {
      this.productsSubMenuMarginSeparator = productsSubmenu.clientHeight;
    }
    if (accountSubmenu) {
      this.accountSubMenuMarginSeparator = accountSubmenu.clientHeight;
    }
  }

  mouseLeaveSubmenu() {
    setTimeout(() => {
      this.productsSubMenuMarginSeparator = 0;
      this.accountSubMenuMarginSeparator = 0;
    }, 0);
  }

  isUserLoggedIn() {
    if (this.cookieService.get('is_user_logged_in') === '1') {
      const loggedInUserInfoObj = JSON.parse(this.cookieService.get('user_auth_information'));
      let userInfoParams = loggedInUserInfoObj.replace(/"|{|}/g, '').split(',');
      let userEmail = userInfoParams[0].split(':')[1];
      let userFirstName = userInfoParams[1].split(':')[1];
      let userLastName = userInfoParams[2].split(':')[1];
      this.loggedInUserEmail = userEmail;
      this.loggedInUserFirstName = userFirstName;
      this.loggedInUserLastName = userLastName;
      if (userFirstName === userLastName) {
        this.loggedInUserLastName = userLastName.substring(0, 1);
      }
      return true;
    }
    return false;
  }

  isUserAdmin() {
    return this.isUserLoggedIn() && this.cookieService.get('is_user_admin') === 'true';
  }

  logoutFromApp() {
    this.authService.logout().subscribe(() => {
      this.closeMobileNavbar();
      this.router.navigate(['/home']);
    }, () => {
      this.router.navigate(['/home']);
    });
  }

  searchProduct() {
    this.searchInput = document.getElementById("search-input");
    let searchValue = this.searchInput.value;
    this.router.navigate(['/products'], {queryParams: {'searchValue': searchValue}});
  }
}
