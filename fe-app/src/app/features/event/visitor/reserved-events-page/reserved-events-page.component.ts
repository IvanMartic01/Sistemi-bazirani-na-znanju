import { Component, OnInit } from '@angular/core';
import {EventResponse} from "../../model/event-response.model";
import {EventService} from "../../../../service/event.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-booked-events-page',
  templateUrl: './reserved-events-page.component.html',
  styleUrls: ['./reserved-events-page.component.scss']
})
export class ReservedEventsPageComponent implements OnInit {

  reservedEvents: Array<EventResponse> = []

  constructor(private eventService: EventService,
              private toastrService: ToastrService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.eventService.getReservedEvents().subscribe(
      events => this.reservedEvents = events,
      error => this.toastrService.error(error.error.message));
  }

  showEvent($event: EventResponse) {
    if (this.reservedEvents.some(event => event.id === $event.id)) {
      this.router.navigate(['book-event', $event.id])
    }
  }
}
