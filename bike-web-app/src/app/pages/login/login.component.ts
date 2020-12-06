import { Component, OnInit, ElementRef, OnDestroy } from '@angular/core';
import { AuthLoginInfo } from 'src/app/auth/login-info';
import { AuthService } from 'src/app/auth/auth.service';
import { TokenStorageService } from 'src/app/auth/token-storage.service';
import { Router } from '@angular/router';
import { Branch } from 'src/app/shared/reference/model/branch';
import { ReferenceService } from 'src/app/shared/reference/reference.service';
import { AdalService } from 'adal-angular4';
import { componentDestroyed, untilComponentDestroyed } from "@w11k/ngx-componentdestroyed";


declare var $: any;

@Component({
  selector: 'app-login-cmp',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})

export class LoginComponent implements OnInit, OnDestroy {
  test: Date = new Date();
  private toggleButton: any;
  private sidebarVisible: boolean;
  private nativeElement: Node;

  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  private loginInfo: AuthLoginInfo;
  branches: Branch[];
  response: any;

  constructor(private element: ElementRef,
    private authService: AuthService,
    private tokenStorage: TokenStorageService,
    private router: Router,
    private refService: ReferenceService,
    private adalService: AdalService) {
    this.nativeElement = element.nativeElement;
    this.sidebarVisible = false;
  }

  ngOnInit() {
    this.adalService.handleWindowCallback();
    if (this.authenticated) {
      this.refService.findAllBranch().pipe(untilComponentDestroyed(this)).subscribe(
        (res: Branch[]) => {
          this.response = res;
          this.branches = this.response.entity.list;
        }
      )
    }

    if (this.tokenStorage.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorage.getAuthorities();
    }

    if (this.isLoggedIn == true) {
      this.goToHomePage();
    }

    var navbar: HTMLElement = this.element.nativeElement;
    this.toggleButton = navbar.getElementsByClassName('navbar-toggle')[0];
    const body = document.getElementsByTagName('body')[0];
    body.classList.add('login-page');
    body.classList.add('off-canvas-sidebar');
    const card = document.getElementsByClassName('card')[0];
    setTimeout(function () {
      // after 1000 ms we add the class animated to the login/register card
      card.classList.remove('card-hidden');
    }, 500);
  }
  sidebarToggle() {
    var toggleButton = this.toggleButton;
    var body = document.getElementsByTagName('body')[0];
    var sidebar = document.getElementsByClassName('navbar-collapse')[0];
    if (this.sidebarVisible == false) {
      setTimeout(function () {
        toggleButton.classList.add('toggled');
      }, 500);
      body.classList.add('nav-open');
      this.sidebarVisible = true;
    } else {
      this.toggleButton.classList.remove('toggled');
      this.sidebarVisible = false;
      body.classList.remove('nav-open');
    }
  }
  ngOnDestroy() {
    const body = document.getElementsByTagName('body')[0];
    body.classList.remove('login-page');
    body.classList.remove('off-canvas-sidebar');
  }
  login() {
    this.adalService.login();
  }
  onSubmit() {
    console.log(this.form);
    this.tokenStorage.saveBranch(this.form.branch);
    /* this.loginInfo = new AuthLoginInfo(
      this.form.username,
      this.form.userpass);

    this.authService.attemptAuth(this.loginInfo).pipe(untilComponentDestroyed(this)).subscribe(
      data => {
        this.tokenStorage.saveToken(data.accessToken);
        this.tokenStorage.saveUsername(data.username);
        this.tokenStorage.saveBranch(this.form.branch);
        this.tokenStorage.saveAuthorities(data.authorities);


        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorage.getAuthorities();
        this.goToHomePage();
      },
      error => {
        console.log(error);
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
      }
    ); */
  }

  goToHomePage() {
    window.location.href = '/dashboard';
  }

  get authenticated(): boolean {
    return this.adalService.userInfo.authenticated;
  }
}
