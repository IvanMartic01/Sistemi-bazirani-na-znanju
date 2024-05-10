import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { LoginFormComponent } from './features/login-register-form/component/login-register-form.component';
import { JwtInterceptor } from './interceptor/jwt.interceptor';
import { SessionExpiredCheckInterceptor } from './interceptor/session-expired-check.interceptor';
import { VisitorNavbarComponent } from './features/navbar/component/visitor-navbar/visitor-navbar.component';
import {AvailableEventsPageComponent} from "./features/event/visitor/available-events-page/available-events-page.component";
import {EventFormComponent} from "./features/event/event-form/event-form.component";
import {AddEventPageComponent} from "./features/event/organizer/add-event-page/component/add-event-page.component";
import {EditEventPageComponent} from "./features/event/organizer/edit-event-page/component/edit-event-page.component";
import { ToastrModule } from 'ngx-toastr';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { OrganizerNavbarComponent } from './features/navbar/component/organizer-navbar/organizer-navbar.component';
import { EventListComponent } from './features/event/event-list/event-list.component';
import { EventDetailsPageComponent } from './features/event/event-details-page/event-details-page.component';
import { FinishedEventPageComponent } from './features/event/organizer/finished-event-page/finished-event-page.component';
import { VisitedEventsPageComponent } from './features/event/visitor/visited-events-page/visited-events-page.component';
import { BookingEventPageComponent } from './features/event/visitor/booking-event-page/booking-event-page.component';
import { ReservedEventsPageComponent } from './features/event/visitor/reserved-events-page/reserved-events-page.component';
import { PendingEventPageComponent } from './features/event/organizer/pending-event-page/pending-event-page.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    AvailableEventsPageComponent,
    EventFormComponent,
    AddEventPageComponent,
    EditEventPageComponent,
    VisitorNavbarComponent,
    OrganizerNavbarComponent,
    EventListComponent,
    EventDetailsPageComponent,
    FinishedEventPageComponent,
    VisitedEventsPageComponent,
    BookingEventPageComponent,
    ReservedEventsPageComponent,
    PendingEventPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: SessionExpiredCheckInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
