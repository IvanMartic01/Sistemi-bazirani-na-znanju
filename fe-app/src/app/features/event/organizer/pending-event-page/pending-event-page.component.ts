import { Component, OnInit } from '@angular/core';
import {EventResponse} from "../../model/event-response.model";
import {EventService} from "../../../../service/event.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";


@Component({
  selector: 'app-pending-event-page',
  templateUrl: './pending-event-page.component.html',
  styleUrls: ['./pending-event-page.component.scss']
})
export class PendingEventPageComponent implements OnInit {

  pendingEvents:Array<EventResponse> = []

  constructor(private eventService:EventService,
              private toastrService:ToastrService,
              private router:Router) { }

  ngOnInit(): void {
    this.eventService.getOrganizerPendingEvents().subscribe(
      events => this.pendingEvents = events,
      error => this.toastrService.error(error.error.message));
  }

  showEvent($event: EventResponse) {
    if (this.pendingEvents.some(event => event.id === $event.id)) {
      this.router.navigate(['edit-event', $event.id])
    } else {
      this.toastrService.error('Event not found')
    }
  }
}
