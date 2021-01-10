import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./home.component";
import {ProductListComponent} from "../product-list/product-list.component";
import {ContactComponent} from "../contact/contact.component";
import {LoginComponent} from "../login/login.component";
import {AuthGuard} from "../../core/guards/auth.guard";


const homeRoutes: Routes = [
  {
    path: 'app',
    component: HomeComponent,
    data: { animationState: 'One' }
  },
  {
    path: 'home',
    component: HomeComponent,
    data: {animationState: 'One'},
    canActivate: [AuthGuard]
  },
  {
    path: 'products',
    component: ProductListComponent,
    data: { animationState: 'Two' }
  },
  {
    path: 'contact',
    component: ContactComponent,
    data: { animationState: 'Three'}
  },
  {
    path: 'login',
    component: LoginComponent,
    data: { animationState: 'Four' }
  }
];

@NgModule({
  imports: [RouterModule.forChild(homeRoutes)],
  exports: [RouterModule]
})
export class HomeRoutingModule { }
