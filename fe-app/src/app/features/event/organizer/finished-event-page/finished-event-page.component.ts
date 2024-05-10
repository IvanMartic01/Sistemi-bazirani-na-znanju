import { Component, OnInit } from '@angular/core';
import {EventResponse} from "../../model/event-response.model";
import {EventService} from "../../../../service/event.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-finished-event-page',
  templateUrl: './finished-event-page.component.html',
  styleUrls: ['./finished-event-page.component.scss']
})
export class FinishedEventPageComponent implements OnInit {

  finishedEvents:Array<EventResponse> = []

  constructor(private eventService:EventService,
              private toastrService:ToastrService,
              private router:Router) { }

  ngOnInit(): void {
    this.eventService.getOrganizerFinishedEvents().subscribe(
      events => this.finishedEvents = events,
      error => this.toastrService.error(error.error.message));
  }

  showEvent($event: EventResponse) {
    if (this.finishedEvents.some(event => event.id === $event.id)) {
      this.router.navigate(['event-details', $event.id])
    } else {
      this.toastrService.error('Event not found')
    }
  }
}
