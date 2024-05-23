import {Component, OnInit} from '@angular/core';
import { AuthService } from 'src/app/service/auth.service';
import {LoginRequest} from "../model/login/login-request.model";
import {TokenResponse} from "../model/login/token-response.model";
import {HttpErrorResponse} from "@angular/common/http";
import {SharedService} from "../../../service/shared.service";
import {Router} from "@angular/router";
import {CreateVisitor} from "../model/create-user/create-visitor.model";
import {VisitorResponse} from "../../../model/user/visitor-response,model";
import {ToastrService} from "ngx-toastr";
import {CountryDto} from "../../../model/response/country-dto.model";
import {CountryService} from "../../../service/country.service";

@Component({
  selector: 'app-login-form',
templateUrl: './login-register-form.component.html',
  styleUrls: ['./login-register-form.component.scss'],
})
export class LoginFormComponent implements OnInit {
  isSignupActive: boolean = true;

  // Login
  loginEmail: string = '';
  loginPassword: string = 'Password!123';

  // Register
  registerEmail: string = '';
  registerPassword: string = '';
  fullName: string = '';

  selectedCountryId: string = "";

  eventTypes: Array<String> = [
    "HIKING",
    "CYCLING",
    "PICNIC",
    "MUSEUM_VISIT",
    "GALLERY_VISIT",
    "CONCERT",
    "ZOO_VISIT",
    "AQUARIUM_VISIT",
    "THEME_PARK_VISIT",
    "BASKETBALL_GAME",
    "FOOTBALL_MATCH",
    "BOXING_MATCH",
    "WELLNESS_CENTER",
    "SPA_TREATMENT",
    "SPA_VISIT",
    "ART_LECTURE",
    "ART_WORKSHOP",
    "PARAGLIDING",
    "BALLOON_RIDE",
    "MICHELIN_STAR_RESTAURANT",
    "MULTIPLE_GENRE_CONCERT"
  ]

  visitorPreferences: Array<{ preference: string, checked: boolean }> = [
    { preference: "OUTDOOR_EVENTS", checked: false },
    { preference: "CULTURAL_EVENTS", checked: false },
    { preference: "FAMILY_EVENTS", checked: false },
    { preference: "SPORTING_EVENTS", checked: false },
    { preference: "RELAXING_EVENTS", checked: false },
    { preference: "ADVENTURE_EVENTS", checked: false },
    { preference: "ARTISTIC_EVENTS", checked: false },
    { preference: "EXOTIC_EVENTS", checked: false },
    { preference: "MUSIC_EVENTS", checked: false },
  ]

  countries: Array<CountryDto> = [];

  constructor(private authService: AuthService,
              private sharedService:SharedService,
              private countryService: CountryService,
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
    const preferences: Array<string> = this.visitorPreferences
      .filter(el => el.checked)
      .map(el => el.preference);
    let createVisitor: CreateVisitor = {
      email: this.registerEmail,
      password: this.registerPassword,
      name: this.fullName,
      countryId: this.selectedCountryId,
      preferences: preferences
    };
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

  ngOnInit(): void {
    this.countryService.getAll().subscribe({
      next: response => {
        this.selectedCountryId = response[0].id;
        this.countries = response;
      }, error: error => {
        if (error instanceof HttpErrorResponse) {
          console.log(error.error.message);
        }
      }
    })
  }

  preferencesOpened: boolean = false;

  updatePreferenceOpened(opened: boolean) {
    this.preferencesOpened = opened;
  }

}
