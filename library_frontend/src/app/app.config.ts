import {
  ApplicationConfig,
  inject,
  provideAppInitializer,
  provideZoneChangeDetection
} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {HttpClient, provideHttpClient, withInterceptors} from '@angular/common/http';
import {basicAuthInterceptor} from './core/helpers/basic-auth.interceptor';
import {Observable} from 'rxjs';
import {UserInfoDto} from './core/models/user-info-dto';

const initializerFn = (): Observable<unknown> | Promise<unknown> | void => {
  const http = inject(HttpClient);

  let authBasicToken = localStorage.getItem("AUTH_BASIC");
  let userInfoSerialized = localStorage.getItem("USER_INFO");

  if (null != authBasicToken) {
    http.get<UserInfoDto>(
      "http://localhost:9922/dashboard-header",
    ).subscribe({
      next(value) {

        let userInfoOk = true;

        let userInfoDto: UserInfoDto | null = null;
        try {
          if (null != userInfoSerialized) {
            userInfoDto = JSON.parse(userInfoSerialized);
          }
        } catch (e) {
          console.warn(e);
        }

        if (null != userInfoDto && null != value) {
          if (userInfoDto.username === value.username) {
            //noop, all good
          } else {
            userInfoOk = false;
          }

          if (userInfoDto.enabled === value.enabled) {
            //noop, all good
          } else {
            userInfoOk = false;
          }

          if (
            null != userInfoDto.authorities
            && null != value.authorities
            && userInfoDto.authorities.length === value.authorities.length
          ) {
            for (let i = 0; i < userInfoDto.authorities.length; i++) {
              let authority = userInfoDto.authorities[i];
              if (value.authorities.includes(authority)) {
                //noop all good
              } else {
                userInfoOk = false;
                break;
              }
            }
          } else {
            userInfoOk = false;
          }

          // if (
          //   null != userInfoDto.identityPhotoFile
          //   && null != value.identityPhotoFile
          // ) {
          //   //noop
          // } else {
          //   userInfoOk = false;
          // }
        } else {
          userInfoOk = false;
        }

        if (true === userInfoOk) {
          //noop, all good
        } else {
          localStorage.removeItem("AUTH_BASIC");
          localStorage.removeItem("USER_INFO");
        }

      },
      error(e) {

        console.error(e);

        localStorage.removeItem("AUTH_BASIC");
        localStorage.removeItem("USER_INFO");

      },
    })
  } else {
    //noop, skip
  }
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    //@NOTE: By default, it uses XMLHttpRequest: https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest
    provideHttpClient(
      withInterceptors([basicAuthInterceptor]),
    ),
    //@NOTE: https://angular.dev/api/core/provideAppInitializer
    provideAppInitializer(initializerFn)
  ]
};
