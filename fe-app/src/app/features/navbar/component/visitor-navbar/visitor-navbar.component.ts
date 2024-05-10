import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../../service/auth.service";
import {SharedService} from "../../../../service/shared.service";
import {Router} from "@angular/router";
@Component({
  selector: 'app-visitor-navbar',
  templateUrl: './visitor-navbar.component.html',
  styleUrls: ['./visitor-navbar.component.scss']
})
export class VisitorNavbarComponent {
  public isSignIn: boolean = false;

  constructor(public authService:AuthService,
              private sharedService:SharedService,
              private router: Router) {
    this.sharedService.isSignIn$.subscribe((value) => {
      this.isSignIn = value;
      this.isSignIn = this.authService.isLoggedIn()
    });
  }

  // VISITOR
  gotAvailableEvents():void {
    this.router.navigate(['available-events'])
  }
  // VISITOR
  goToVisitedEvents():void {
    this.router.navigate(['visited-events'])
  }

  goToReservedEvents():void {
    this.router.navigate(['reserved-events'])
  }


  signOut(): void {
    this.isSignIn = false;
    this.authService.signOut();
    this.router.navigate([''])
  }

}
