import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

console.log('the application starts here...');

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));