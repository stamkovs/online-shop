import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home.component';
import {ProductListComponent} from '../products/product-list/product-list.component';
import {ContactComponent} from '../contact/contact.component';
import {EntryComponent} from '../entry/entry.component';
import {AuthGuard} from '../../core/guards/auth.guard';
import {ProductListResolver} from '../products/ProductListResolver';

const homeRoutes: Routes = [
  {
    path: 'app',
    component: HomeComponent,
    data: {animationState: 'One'}
  },
  {
    path: 'home',
    component: HomeComponent,
    data: {animationState: 'One'},
  },
  {
    path: 'products',
    component: ProductListComponent,
    data: {
      animationState: 'Two',
    },
    resolve: {
      productsData: ProductListResolver
    },
  },
  {
    path: 'products/:productCategory',
    component: ProductListComponent,
    resolve: {
      productsData: ProductListResolver,
    },
  },
  {
    path: 'contact',
    component: ContactComponent,
    data: {animationState: 'Three'}
  },
  {
    path: 'entry',
    component: EntryComponent,
    data: {animationState: 'Four'},
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [RouterModule.forChild(homeRoutes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
