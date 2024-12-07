import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  private _isAuthenticated = false;

  login(loginObj: Object) {

    return this.http.post("http://localhost:9922/login", loginObj);

  }

  logout() {
    //#todo
  }

  isAuthenticated(): boolean {
    return this._isAuthenticated;
  }
}
