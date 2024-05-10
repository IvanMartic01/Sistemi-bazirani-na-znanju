import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../../../service/auth.service";
import {SharedService} from "../../../../service/shared.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-organizer-navbar',
  templateUrl: './organizer-navbar.component.html',
  styleUrls: ['./organizer-navbar.component.scss']
})
export class OrganizerNavbarComponent  {

  public isSignIn: boolean = false;

  constructor(public authService:AuthService,
              private sharedService:SharedService,
              private router: Router) {
    this.sharedService.isSignIn$.subscribe((value) => {
      this.isSignIn = value;
      this.isSignIn = this.authService.isLoggedIn()
    });
  }

  goToAddNewEvent():void {
    this.router.navigate(['add-event'])
  }

  goToFinishedEvents():void {
    this.router.navigate(['finished-events'])
  }

  goToPendingEvents():void {
    this.router.navigate(['pending-events'])
  }

  signOut(): void {
    this.isSignIn = false;
    this.authService.signOut();
    this.router.navigate([''])
  }
}
