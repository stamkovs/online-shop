import {Component, ViewEncapsulation} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {routeTransitionAnimations} from './route-transition-animation';
import {AuthService} from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations: [routeTransitionAnimations]

})
export class AppComponent {
  title = 'Shoptastic';

  constructor(private authService: AuthService) {

  }

  prepareRoute(outlet: RouterOutlet) {
    return outlet &&
      outlet.activatedRouteData &&
      outlet.activatedRouteData['animationState'];
  }

  changeOfRoutes() {
    this.authService.isLoggedIn().subscribe((isLoggedIn: any) => {

    }, error => {

    });
  }
}
