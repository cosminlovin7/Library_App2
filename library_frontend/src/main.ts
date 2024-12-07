import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';

console.log('the application starts here...');

bootstrapApplication(AppComponent, appConfig)
  .then(() => {
    const loader = document.getElementById('app-loading');
    if (loader) {
      setTimeout(() => {
        loader.style.display = 'none';
      }, 500);
    }
  })
  .catch(err => console.error(err));
