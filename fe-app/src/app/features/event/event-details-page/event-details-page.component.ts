import { Component, OnInit } from '@angular/core';
import {EventService} from "../../../service/event.service";
import {ActivatedRoute} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {EventResponse} from "../model/event-response.model";

@Component({
  selector: 'app-event-details-page',
  templateUrl: './event-details-page.component.html',
  styleUrls: ['./event-details-page.component.scss']
})
export class EventDetailsPageComponent implements OnInit {

  event:EventResponse = {} as EventResponse;

  constructor(private eventService:EventService,
              private route: ActivatedRoute,
              private toastrService:ToastrService) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.toastrService.error('Event id is not provided');
      return;
    }
    this.eventService.getEventById(id).subscribe(
      event => this.event = event,
      error => {
        alert("asdasd")
        this.toastrService.error(error.error.message)}
      )
  }
}
