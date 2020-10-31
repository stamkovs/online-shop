import {AfterViewInit, Component, ElementRef, HostListener, OnInit, Renderer2, ViewEncapsulation} from '@angular/core';

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
  navbarItems: NodeListOf<HTMLElement>;
  subMenuMarginSeparator: number;
  screenHeight: number;
  screenWidth: number;

  constructor() { }

  ngOnInit() {
    this.navbar = document.getElementById("navbar");
    this.navbarToggle = this.navbar.querySelector(".navbar-toggle");
    this.navbarMenu = this.navbar.querySelector(".navbar-menu");
    this.navbarLinksContainer = this.navbar.querySelector(".navbar-links");
    this.navbarItems = document.querySelectorAll(".navbar-item");
    // console.log(this.navbarItems);

    this.navbarItems.forEach(item => {
      item.addEventListener("click", this.toggleActiveMenu);
    })
  }

  openMobileNavbar() {
    this.navbar.classList.add("opened");
    this.navbarToggle.setAttribute("aria-label", "Close navigation menu.");
  }

  closeMobileNavbar() {
    if (this.screenWidth < 769 && this.navbar.classList.contains("opened")) {
      this.navbar.classList.remove("opened");
      this.navbarToggle.setAttribute("aria-label", "Open navigation menu.");
    }
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

  toggleActiveMenu(event) {
    let navItems = document.querySelectorAll(".navbar-link");
    navItems.forEach(item => {
      if (item.classList.contains("active")) {
        item.classList.remove("active");
      }
    });
    console.log(event.target);
    event.target.classList.add("active");
  }

}
