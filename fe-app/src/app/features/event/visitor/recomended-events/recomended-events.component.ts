import { Component, OnInit } from '@angular/core';
import {EventResponse} from "../../model/event-response.model";
import {EventService} from "../../../../service/event.service";
import {ToastrService} from "ngx-toastr";
import {Router} from "@angular/router";

@Component({
  selector: 'app-recomended-events',
  templateUrl: './recomended-events.component.html',
  styleUrls: ['./recomended-events.component.scss']
})
export class RecomendedEventsComponent implements OnInit {

  recommendedEvents:Array<EventResponse> = [];

  constructor(private eventService:EventService,
              private toastrService:ToastrService,
              private router:Router) { }

  ngOnInit(): void {
    this.eventService.getRecommendedEvents().subscribe(
      events => this.recommendedEvents = events,
      error => this.toastrService.error(error.error.message));
  }

  goToEventDetailsPage($event:EventResponse): void {
    this.router.navigate(['book-event', $event.id]);
  }
}
