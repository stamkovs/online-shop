import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HomeRoutingModule} from './components/home/home-routing.module';
import {ConfirmAccountComponent} from './components/entry/confirm-account/confirm-account.component';

const appRoutes: Routes = [
  {
    path: 'confirm-account',
    component: ConfirmAccountComponent
  },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/home'
  }
];

@NgModule({
  imports: [BrowserAnimationsModule, RouterModule.forRoot(appRoutes), HomeRoutingModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
