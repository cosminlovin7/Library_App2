import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient} from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    //@NOTE: By default, it uses XMLHttpRequest: https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest
    provideHttpClient(),
  ]
};
