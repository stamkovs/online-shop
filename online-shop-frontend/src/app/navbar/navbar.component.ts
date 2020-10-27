import {AfterViewInit, Component, HostListener, OnInit, ViewEncapsulation} from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NavbarComponent implements OnInit, AfterViewInit {
  navbar: HTMLElement;
  navbarToggle: HTMLElement;
  navbarMenu: HTMLElement;
  navbarLinksContainer: HTMLElement;
  subMenuMarginSeparator: number;
  screenHeight: number;
  screenWidth: number;

  constructor() { }

  ngOnInit(): void {
    this.navbar = document.getElementById("navbar");
    this.navbarToggle = this.navbar.querySelector(".navbar-toggle");
    this.navbarMenu = this.navbar.querySelector(".navbar-menu");
    this.navbarLinksContainer = this.navbar.querySelector(".navbar-links");
  }

  openMobileNavbar() {
    this.navbar.classList.add("opened");
    this.navbarToggle.setAttribute("aria-label", "Close navigation menu.");
  }

  closeMobileNavbar() {
    this.navbar.classList.remove("opened");
    this.navbarToggle.setAttribute("aria-label", "Open navigation menu.");
  }

  ngAfterViewInit() {
    this.navbarToggle.addEventListener("click", () => {
      if (this.navbar.classList.contains("opened")) {
        this.closeMobileNavbar();
      } else {
        this.openMobileNavbar();
      }
    });

    this.navbarLinksContainer.addEventListener("click", (clickEvent) => {
      clickEvent.stopPropagation();
    });

    this.navbarMenu.addEventListener("click", this.closeMobileNavbar);
  }

  @HostListener('window:load', ['$event'])
  getScreenSizeOnLoad(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  @HostListener('window:resize', ['$event'])
  getScreenSizeOnResize(event?) {
    this.screenHeight = window.innerHeight;
    this.screenWidth = window.innerWidth;
  }

  mouseEnterSubMenu(event) {
    if (this.screenWidth > 768) {
      return;
    }
    const clickedItem = event.target;
    const submenu = clickedItem.querySelector(".submenu");
    this.subMenuMarginSeparator = submenu.clientHeight;
  }

  mouseLeaveSubmenu() {
    this.subMenuMarginSeparator = 0;
  }

}
