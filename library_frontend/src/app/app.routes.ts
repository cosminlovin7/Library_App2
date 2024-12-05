import { Routes } from '@angular/router';
import {PageNotFoundComponent} from './core/pages/page-not-found/page-not-found.component';
import {MainPageComponent} from './core/pages/main-page/main-page.component';
import {LoginPageComponent} from './core/pages/login-page/login-page.component';
import {authGuard} from './core/guards/auth.guard';

export const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
  {
    path: 'dashboard',
    component: MainPageComponent,
    canActivate: [authGuard],
    children: [
      // {
      //   path: 'books',
      //   component: #todo
      // }
    ]
  },
  {path: 'login', component: LoginPageComponent},
  {path: '**', component: PageNotFoundComponent},
];
