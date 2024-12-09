import {Injectable, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {map} from 'rxjs';
import {LoginInfoDto} from '../models/login-info-dto';
import {UserInfoDto} from '../models/user-info-dto';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) {
    let me = this;

    me.init();
  }

  private _userInfo: UserInfoDto | null = null;
  private _isAuthenticated = false;
  private _isEnabled = false;

  login(loginObj: LoginInfoDto) {
    let me = this;
    return me.http.post<UserInfoDto>("http://localhost:9922/login", loginObj)
      .pipe(map(user => {
        let authBasicToken = null;
        if (null != loginObj.username && null != loginObj.password) {

          authBasicToken = me.buildAuthBasicToken(loginObj.username, loginObj.password);

        }

        if (null != user && null != authBasicToken) {

          localStorage.setItem("AUTH_BASIC", authBasicToken);
          localStorage.setItem("USER_INFO", JSON.stringify(user));

          me._userInfo = user;
          me._isAuthenticated = true;
          me._isEnabled = (null != user.enabled ? user.enabled : false);

        }
        else {

          console.warn('Error occurred while logging in...');

        }

        return user;
      }));
  }

  private buildAuthBasicToken(username: string, password: string): string | null{
    if (null == username) {
      return null;
    }
    if (null == password) {
      return null;
    }

    let authBasicToken = btoa(username + ":" + password);

    return authBasicToken;
  }

  logout() {
    let me = this;

    localStorage.removeItem("AUTH_BASIC");
    localStorage.removeItem("USER_INFO");

    me._userInfo = null;
    me._isAuthenticated = false;
    me._isEnabled = false;

    return true;
  }

  isAuthenticated(): boolean {
    let me = this;
    return me._isAuthenticated;
  }

  getUsername(): string | null {
    let me = this;

    if (null == me._userInfo) {
      return null;
    }

    return me._userInfo.username;
  }

  isEnabled(): boolean {
    let me = this;
    return me._isEnabled;
  }

  hasRoleAdmin(): boolean {
    let me = this;

    let isUserAdmin = false;

    if (
      null != me._userInfo
      && null != me._userInfo.authorities
    ) {
      if (me._userInfo.authorities.includes("ROLE_ADMIN")) {
        isUserAdmin = true;
      }
    }

    return isUserAdmin;
  }

  getAuthBasicToken(): string | null {
    return localStorage.getItem("AUTH_BASIC");
  }

  getUserAuthorities(): string[] {
    let me = this;
    let out: string[] = [];

    if (null != me._userInfo && null != me._userInfo.authorities) {

      me._userInfo.authorities.forEach(authority => {
        out.push(authority);
      })

    }

    return out;
  }

  init() {

    console.log('init function called');
    let me = this;
    let authBasicToken = localStorage.getItem("AUTH_BASIC");
    let userInfoSerialized = localStorage.getItem("USER_INFO");

    if (null != authBasicToken && null != userInfoSerialized) {

      let userInfoDto: UserInfoDto | null = null;

      try {
        userInfoDto = JSON.parse(userInfoSerialized);
      } catch (e) {
        console.warn(e);
      }

      if (null != userInfoDto) {

        me._userInfo = userInfoDto;
        me._isAuthenticated = true;
        me._isEnabled = (null != userInfoDto.enabled ? userInfoDto.enabled : false);

      }

    }
    else if (null == authBasicToken && null == userInfoSerialized) {
      //noop, all good, it means the user is not logged in
    }
    else {
      console.error('Bad logic. User session is invalid');
      localStorage.removeItem("AUTH_BASIC");
      localStorage.removeItem("USER_INFO");

      me._userInfo = null;
      me._isAuthenticated = false;
      me._isEnabled = false;
    }

  }
}
