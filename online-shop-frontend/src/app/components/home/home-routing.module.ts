import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home.component';
import {ProductListComponent} from '../products/product-list/product-list.component';
import {ContactComponent} from '../contact/contact.component';
import {EntryComponent} from '../entry/entry.component';
import {AuthGuard} from '../../core/guards/auth.guard';
import {ProductListResolver} from '../products/ProductListResolver';
import {ProductDetailComponent} from '../products/product-detail/product-detail.component';
import {ProductDetailResolver} from '../products/ProductDetailResolver';

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
    resolve: {
      newestProducts: ProductListResolver,
    }
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
    path: 'products/:productCategory/:id',
    component: ProductDetailComponent,
    resolve: {
      productDetail: ProductDetailResolver,
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
