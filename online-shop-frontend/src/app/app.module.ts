import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AppComponent} from './app.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import {HomeComponent} from './components/home/home.component';
import {EntryComponent} from './components/entry/entry.component';
import {ContactComponent} from './components/contact/contact.component';
import {ConfirmAccountComponent} from './components/entry/confirm-account/confirm-account.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {EntryService} from './services/entry.service';
import {CookieModule} from 'ngx-cookie';
import {AppRoutingModule} from './app-routing.module';
import {AuthService} from './services/auth.service';
import {ErrorInterceptor} from './core/interceptor/error.interceptor';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {WindowService} from './services/window.service';
import {ConfirmAccountService} from './services/confirm-account.service';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatDialogModule} from '@angular/material/dialog';
import {DialogComponent} from './components/dialog/dialog.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {ForgotPasswordService} from './services/forgot-password.service';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {SpinnerService} from './services/spinner.service';
import {HttpRequestInterceptor} from './core/interceptor/http-request-interceptor.service';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {SpinnerComponent} from './components/spinner/spinner.component';
import {MatTableModule} from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import {ProductCardComponent} from './components/products/product-card/product-card.component';
import {ProductListResolver} from './components/products/ProductListResolver';
import {ProductService} from './services/product.service';
import {ProductListComponent} from './components/products/product-list/product-list.component';
import {ProductDetailComponent} from './components/products/product-detail/product-detail.component';
import {ProductDetailResolver} from './components/products/ProductDetailResolver';
import { BreadcrumbsComponent } from './components/breadcrumbs/breadcrumbs.component';
import { CartComponent } from './components/cart/cart.component';
import {CartService} from './services/cart.service';
import {ContactService} from './services/contact.service';
import { WishlistComponent } from './components/wishlist/wishlist.component';
import {WishlistService} from './services/wishlist.service';
import {WishlistResolver} from './components/wishlist/WishlistResolver';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    EntryComponent,
    ProductListComponent,
    ContactComponent,
    ConfirmAccountComponent,
    DialogComponent,
    ForgotPasswordComponent,
    SpinnerComponent,
    ProductCardComponent,
    ProductDetailComponent,
    BreadcrumbsComponent,
    CartComponent,
    WishlistComponent,
  ],
  imports: [
    BrowserModule,
    CookieModule.forRoot(),
    CommonModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule],
  providers: [
    AuthService,
    WindowService,
    EntryService,
    ConfirmAccountService,
    ForgotPasswordService,
    SpinnerService,
    ProductListResolver,
    ProductDetailResolver,
    ProductService,
    CartService,
    ContactService,
    WishlistService,
    WishlistResolver,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpRequestInterceptor,
      multi: true
    },
  ],
  bootstrap: [AppComponent],
  entryComponents: [DialogComponent]
})
export class AppModule {
}
