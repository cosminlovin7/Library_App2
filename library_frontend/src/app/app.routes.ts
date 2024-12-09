import { Routes } from '@angular/router';
import {PageNotFoundComponent} from './core/pages/page-not-found/page-not-found.component';
import {MainPageComponent} from './core/pages/main-page/main-page.component';
import {LoginPageComponent} from './core/pages/login-page/login-page.component';
import {authLoginGuard} from './core/helpers/auth-login.guard';
import {authDashboardGuard} from './core/helpers/auth-dashboard.guard';
import {AllUsersPageComponent} from './core/pages/all-users-page/all-users-page.component';

export const routes: Routes = [
  {path: '', redirectTo: '/dashboard', pathMatch: 'full',},
  {
    path: 'dashboard',
    component: MainPageComponent,
    canActivate: [authDashboardGuard],
    children: [
      {
        path: 'users',
        component: AllUsersPageComponent
      }
    ]
  },
  {path: 'login', component: LoginPageComponent, canActivate: [authLoginGuard]},
  {path: '**', component: PageNotFoundComponent},
];
