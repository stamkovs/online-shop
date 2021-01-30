import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule} from '@angular/common';

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { EntryComponent } from './components/entry/entry.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ContactComponent } from './components/contact/contact.component';
import {ConfirmAccountComponent} from './components/entry/confirm-account/confirm-account.component';
import {FormsModule} from '@angular/forms';
import {EntryService} from './services/entry.service';
import {CookieModule} from 'ngx-cookie';
import {AppRoutingModule} from './app-routing.module';
import {AuthService} from './services/auth.service';
import {ErrorInterceptor} from './core/interceptor/error.interceptor';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {WindowService} from './services/window.service';
import {ConfirmAccountService} from './services/confirm-account.service';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    EntryComponent,
    ProductListComponent,
    ContactComponent,
    ConfirmAccountComponent,
  ],
  imports: [
    BrowserModule, CookieModule.forRoot(), CommonModule, AppRoutingModule, HttpClientModule, FormsModule
  ],
  providers: [AuthService, WindowService, EntryService, ConfirmAccountService,
    {
     provide: HTTP_INTERCEPTORS,
     useClass: ErrorInterceptor,
     multi: true
    }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
