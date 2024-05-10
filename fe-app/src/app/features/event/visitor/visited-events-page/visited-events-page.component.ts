import { Component, OnInit } from '@angular/core';
import {EventService} from "../../../../service/event.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";
import {EventResponse} from "../../model/event-response.model";

@Component({
  selector: 'app-visitor-events-page',
  templateUrl: './visited-events-page.component.html',
  styleUrls: ['./visited-events-page.component.scss']
})
export class VisitedEventsPageComponent implements OnInit {

  visitedEvents:Array<EventResponse> = []

  constructor(private eventService:EventService,
              private toastrService:ToastrService,
              private router:Router) { }

  ngOnInit(): void {
    this.eventService.getVisitedEvents().subscribe(
      events => this.visitedEvents = events,
      error => this.toastrService.error(error.error.message));
  }

  showEvent($event: EventResponse) {
    if (this.visitedEvents.some(event => event.id === $event.id)) {
      this.router.navigate(['event-details', $event.id])
    }
  }
}
