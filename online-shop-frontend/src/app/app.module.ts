import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CommonModule} from "@angular/common";

import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { HomeComponent } from './components/home/home.component';
import { EntryComponent } from './components/entry/entry.component';
import { ProductListComponent } from './components/product-list/product-list.component';
import { ContactComponent } from './components/contact/contact.component';
import {AppRoutingModule} from "./app-routing.module";
import {AuthService} from "./services/auth.service";
import {WindowService} from "./services/window.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {ErrorInterceptor} from "./core/interceptor/error.interceptor";
import {CookieModule} from "ngx-cookie";

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomeComponent,
    EntryComponent,
    ProductListComponent,
    ContactComponent,
  ],
  imports: [
    BrowserModule, CookieModule.forRoot(), CommonModule, AppRoutingModule, HttpClientModule
  ],
  providers: [AuthService, WindowService,
    {
     provide: HTTP_INTERCEPTORS,
     useClass: ErrorInterceptor,
     multi: true
    }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
