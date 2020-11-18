import {Component, ViewEncapsulation} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {routeTransitionAnimations} from "./route-transition-animation";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  encapsulation: ViewEncapsulation.None,
  animations: [routeTransitionAnimations]

})
export class AppComponent {
  title = 'src';

  prepareRoute(outlet: RouterOutlet) {
    return outlet &&
      outlet.activatedRouteData &&
      outlet.activatedRouteData['animationState'];
  }
}
