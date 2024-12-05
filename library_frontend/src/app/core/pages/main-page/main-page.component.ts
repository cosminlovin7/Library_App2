import { Component } from '@angular/core';
import {CustomFooterComponent} from '../../../shared/components/custom-footer/custom-footer.component';
import {CustomHeaderComponent} from '../../../shared/components/custom-header/custom-header.component';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-main-page',
  imports: [
    CustomFooterComponent,
    CustomHeaderComponent,
    RouterOutlet
  ],
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})
export class MainPageComponent {

}
