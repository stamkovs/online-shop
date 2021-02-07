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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {EntryService} from './services/entry.service';
import {CookieModule} from 'ngx-cookie';
import {AppRoutingModule} from './app-routing.module';
import {AuthService} from './services/auth.service';
import {ErrorInterceptor} from './core/interceptor/error.interceptor';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {WindowService} from './services/window.service';
import {ConfirmAccountService} from './services/confirm-account.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDialogModule} from '@angular/material/dialog';
import {DialogComponent} from './components/dialog/dialog.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {ForgotPasswordService} from './services/forgot-password.service';
import {MatSnackBarModule} from '@angular/material/snack-bar';

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
    ForgotPasswordComponent
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
    MatSnackBarModule],
  providers: [AuthService, WindowService, EntryService, ConfirmAccountService, ForgotPasswordService,
    {
     provide: HTTP_INTERCEPTORS,
     useClass: ErrorInterceptor,
     multi: true
    }
    ],
  bootstrap: [AppComponent],
  entryComponents: [DialogComponent]
})
export class AppModule { }
