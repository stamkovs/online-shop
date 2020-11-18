import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HomeRoutingModule} from "./home/home-routing.module";

const appRoutes: Routes = [
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
