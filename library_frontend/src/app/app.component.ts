import { Component } from '@angular/core';
import {MainPageComponent} from './core/pages/main-page/main-page.component';
import {LoginPageComponent} from './core/pages/login-page/login-page.component';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  //#todo
}
