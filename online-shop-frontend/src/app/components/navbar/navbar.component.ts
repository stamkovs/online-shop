import {
  AfterViewInit,
  Component,
  HostListener,
  OnInit,
} from '@angular/core';
import {CookieService} from 'ngx-cookie';
import {AuthService} from '../../services/auth.service';

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
  navbarItems: NodeListOf<HTMLElement>;
  subMenuMarginSeparator: number;
  screenHeight: number;
  screenWidth: number;

  constructor(private cookieService: CookieService, private authService: AuthService) { }

  ngOnInit() {
    this.navbar = document.getElementById('navbar');
    this.navbarToggle = this.navbar.querySelector('.navbar-toggle');
    this.navbarMenu = this.navbar.querySelector('.navbar-menu');
    this.navbarLinksContainer = this.navbar.querySelector('.navbar-links');
    this.navbarItems = document.querySelectorAll('.navbar-item');
    this.subMenuMarginSeparator = 0;

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
      this.subMenuMarginSeparator = 0;
      return;
    }
    const clickedItem = event.target;
    const submenu = clickedItem.querySelector('.submenu');
    this.subMenuMarginSeparator = submenu.clientHeight;
  }

  mouseLeaveSubmenu() {
    this.subMenuMarginSeparator = 0;
  }

  isUserLoggedIn() {
    return this.cookieService.get('is_user_logged_in') === '1';
  }

  logoutFromApp() {
    this.authService.logout().subscribe((data: any) => {

    }, error => {

    });
  }
}
