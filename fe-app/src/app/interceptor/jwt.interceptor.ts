import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor() {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    // Add your login and register endpoints here
    const bypassedUrls = ['/login', '/register'];

    if (bypassedUrls.some(url => request.url.includes(url))) {
      console.log("skipping jwt interceptor");
      return next.handle(request);
    }

    console.log("jwt interceptor")

    let accessToken: any = localStorage.getItem('jwt');

    if (accessToken) {
      accessToken = accessToken.replace(/^"(.*)"$/, '$1');
    }
    if (accessToken) {
      const cloned = request.clone({
        headers: request.headers.set('Authorization', 'Bearer ' + accessToken),
      });

      return next.handle(cloned);
    } else {
      return next.handle(request);
    }
  }
}
