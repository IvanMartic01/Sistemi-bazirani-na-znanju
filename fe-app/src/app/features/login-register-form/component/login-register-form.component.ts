import { Component } from '@angular/core';
import { AuthService } from 'src/app/service/auth.service';
import {LoginRequest} from "../model/login/login-request.model";
import {TokenResponse} from "../model/login/token-response.model";
import {HttpErrorResponse} from "@angular/common/http";
import {SharedService} from "../../../service/shared.service";
import {Router} from "@angular/router";
import {CreateVisitor} from "../model/create-user/create-visitor.model";
import {VisitorResponse} from "../../../model/user/visitor-response,model";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login-form',
templateUrl: './login-register-form.component.html',
  styleUrls: ['./login-register-form.component.scss'],
})
export class LoginFormComponent {
  isSignupActive: boolean = true;

  // Login
  loginEmail: string = '';
  loginPassword: string = 'Password!123';

  // Register
  registerEmail: string = '';
  registerPassword: string = '';
  fullName: string = '';

  constructor(private authService: AuthService,
              private sharedService:SharedService,
              private toastrService:ToastrService,
              private router: Router) {}

  login() {
    localStorage.removeItem('jwt');
    console.log(this.loginEmail, this.loginPassword);

    let loginRequest: LoginRequest = {
      email: this.loginEmail,
      password: this.loginPassword,
    };

    this.authService.login(loginRequest).subscribe({
      next: (response: TokenResponse) => {
        console.log(response);
        localStorage.setItem('jwt', JSON.stringify(response.token));
        this.sharedService.setIsSignIn(true)
        if (this.authService.getRole() == 'ORGANIZER') {
          this.router.navigate(['add-event']);
        } else if (this.authService.getRole() == 'VISITOR') {
          this.router.navigate(['available-events']);
        }
      },
      error: (error: HttpErrorResponse) => {
          alert(`Status Code: ${error.status}\nMessage: ${error.error.message}`);
      },
    });
  }

  signup() {
    let createVisitor: CreateVisitor = {
      email: this.registerEmail,
      password: this.registerPassword,
      name: this.fullName
    }
    console.log(createVisitor);

    this.authService.creteVisitorAccount(createVisitor).subscribe({
      next: (response:VisitorResponse) => {
        console.log(response);
        this.sharedService.setIsSignIn(true);
        this.toastrService.success('Account created successfully');
        this.router.navigate(['']);
      },
      error: (error: HttpErrorResponse) => {
        alert(`Status Code: ${error.status}\nMessage: ${error.error.message}`);
      },
    });

  }

  toggleSignup() {
    this.isSignupActive = !this.isSignupActive;
  }
}
