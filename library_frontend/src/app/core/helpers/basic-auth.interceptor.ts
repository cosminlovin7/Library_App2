import { HttpInterceptorFn } from '@angular/common/http';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authBasicToken = localStorage.getItem("AUTH_BASIC");

  if (null != authBasicToken) {
    req = req.clone({
      setHeaders: {
        Authorization: `Basic ${authBasicToken}`
      }
    });
  }

  return next(req);
};
