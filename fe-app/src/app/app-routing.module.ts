import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginFormComponent } from './features/login-register-form/component/login-register-form.component';
import {AvailableEventsPageComponent} from "./features/event/visitor/available-events-page/available-events-page.component";
import {AddEventPageComponent} from "./features/event/organizer/add-event-page/component/add-event-page.component";
import {EditEventPageComponent} from "./features/event/organizer/edit-event-page/component/edit-event-page.component";
import {OrganizerPagesGuard} from "./guards/organizer-pages-guard.service";
import {NotAuthorizedGuard} from "./guards/not-authorized.guard";
import {VisitorPagesGuard} from "./guards/visitor-pages-guard.service";
import {EventDetailsPageComponent} from "./features/event/event-details-page/event-details-page.component";
import {VisitedEventsPageComponent} from "./features/event/visitor/visited-events-page/visited-events-page.component";
import {
  FinishedEventPageComponent
} from "./features/event/organizer/finished-event-page/finished-event-page.component";
import {BookingEventPageComponent} from "./features/event/visitor/booking-event-page/booking-event-page.component";
import {
  ReservedEventsPageComponent
} from "./features/event/visitor/reserved-events-page/reserved-events-page.component";
import {PendingEventPageComponent} from "./features/event/organizer/pending-event-page/pending-event-page.component";
import {RecomendedEventsComponent} from "./features/event/visitor/recomended-events/recomended-events.component";

const routes: Routes = [
  { path: '', component: LoginFormComponent }, // canActivate: [NotAuthorizedGuard]
  { path: 'event-details/:id', component: EventDetailsPageComponent }, // canActivate: [VisitorPagesGuard]

  { path: 'available-events', component: AvailableEventsPageComponent }, // canActivate: [VisitorPagesGuard]
  { path: 'recommended-events', component: RecomendedEventsComponent }, // canActivate: [VisitorPagesGuard]
  { path: 'visited-events', component: VisitedEventsPageComponent },
  { path: 'reserved-events', component: ReservedEventsPageComponent },

  { path: 'finished-events', component: FinishedEventPageComponent, },
  { path: 'pending-events', component: PendingEventPageComponent, },
  { path: 'add-event', component: AddEventPageComponent, }, // canActivate: [OrganizerPagesGuard]
  { path: 'edit-event/:id', component: EditEventPageComponent, }, // canActivate: [OrganizerPagesGuard]
  { path: 'book-event/:id', component: BookingEventPageComponent, }, // canActivate: [VisitorPagesGuard]
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
