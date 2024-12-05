import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  private _isAuthenticated = false;

  login() {
    //#todo
  }

  logout() {
    //#todo
  }

  isAuthenticated(): boolean {
    return this._isAuthenticated;
  }
}
