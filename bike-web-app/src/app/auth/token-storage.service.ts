import { Injectable } from '@angular/core';
import { User } from '../model/user';

const TOKEN_KEY = 'AuthToken';
const USERNAME_KEY = 'AuthUsername';
const USER_KEY = 'AuthUser';
const LOGIN_STATUS = 'Login Status';

@Injectable({
  providedIn: 'root'
})
export class TokenStorageService {
  private roles: Array<string> = [];

  private static ROLES: Array<string> = null;

  constructor() { }

  signOut() {
    window.sessionStorage.clear();
  }

  public saveToken(token: string) {
    window.sessionStorage.removeItem(TOKEN_KEY);
    window.sessionStorage.setItem(TOKEN_KEY, token);
  }

  public getToken(): string {
    return sessionStorage.getItem(TOKEN_KEY);
  }

  public saveUsername(username: string) {
    window.sessionStorage.removeItem(USERNAME_KEY);
    window.sessionStorage.setItem(USERNAME_KEY, username);
  }

  public getUsername(): string {
    return sessionStorage.getItem(USERNAME_KEY);
  }

  private static hasValue(val: string): boolean {
    return val !== null && val !== 'undefined' && val !== 'null';
  }


  public saveUser(user: User) {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
  }

  public getUser() {
    return JSON.parse(sessionStorage.getItem(USER_KEY));
  }

  public saveLoginStatus() {
    window.sessionStorage.removeItem(LOGIN_STATUS);
    window.sessionStorage.setItem(LOGIN_STATUS, JSON.stringify("Signed In"));
  }

  public getLoginStatus() {
    return JSON.parse(sessionStorage.getItem(LOGIN_STATUS));
  }
}
