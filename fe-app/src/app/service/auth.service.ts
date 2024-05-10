import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Route, Router } from '@angular/router';
import {LoginRequest} from "../features/login-register-form/model/login/login-request.model";
import {TokenResponse} from "../features/login-register-form/model/login/token-response.model";
import {VisitorResponse} from "../model/user/visitor-response,model";
import {CreateVisitor} from "../features/login-register-form/model/create-user/create-visitor.model";
import {CreateOrganizer} from "../features/login-register-form/model/create-user/create-organizer.model";
import {OrganizerResponse} from "../model/user/organizer-response.model";
import {SharedService} from "./shared.service";


// TODO implement reset password
// TODO implement block organizer
// TODO implement unblock organizer
// TODO implement register organizer

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private httpClient: HttpClient,
              private router: Router,
              private sharedService: SharedService) {}

  public login(loginRequest: LoginRequest): Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(
      `${environment.baseUrl}/auth/generate-token`,
      loginRequest
    );
  }

  public creteVisitorAccount(
    createVisitor: CreateVisitor
  ): Observable<VisitorResponse> {
    return this.httpClient.post<VisitorResponse>(
      `${environment.baseUrl}/auth/register-visitor`,
      createVisitor
    );
  }

  public creteOrganizerAccount(
    createAccountRequest: CreateOrganizer
  ): Observable<OrganizerResponse> {
    return this.httpClient.post<OrganizerResponse>(
      `${environment.baseUrl}/register-organizer`,
      createAccountRequest
    );
  }


  public signOut(): void {
    localStorage.removeItem('jwt');
    this.sharedService.setIsSignIn(false);
    this.router.navigate(['']);
  }

  public getRole(): string {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const helper = new JwtHelperService();
      const role = helper.decodeToken(accessToken).roles;
      return role;
    }
    return '';
  }

  public getId(): String {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const helper = new JwtHelperService();
      const id = helper.decodeToken(accessToken).id;
      return id;
    }
    return '';
  }

  public getEmail(): string {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const helper = new JwtHelperService();
      const email = helper.decodeToken(accessToken).sub;
      return email;
    }
    return '';
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('jwt') != null;
  }

  private getToken(): string {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('jwt');
      const decodedItem = JSON.parse(accessToken);
      return decodedItem.accessToken;
    }
    return '';
  }
}
