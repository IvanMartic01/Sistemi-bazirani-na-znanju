import {Component, OnInit} from '@angular/core';
import {EventService} from "../../../../service/event.service";
import {Router} from "@angular/router";
import {EventResponse} from "../../model/event-response.model";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-events-page',
  templateUrl: './available-events-page.component.html',
  styleUrls: ['./available-events-page.component.scss'],
})
export class AvailableEventsPageComponent implements OnInit {

  availableEvents:Array<EventResponse> = [];
  showNoEventsMessage: boolean = false;

  constructor(private eventService:EventService,
              private toastrService:ToastrService,
              private router:Router) { }

  ngOnInit(): void {
    this.eventService.getAllAvailableEvents().subscribe(
      events => {
        this.availableEvents = events
        if (this.availableEvents.length <= 0) {
          this.showNoEventsMessage = true;
        }
      },
      error => {
        this.toastrService.error(error.error.message)
        this.showNoEventsMessage = true;
      n});
  }

  goToEventDetailsPage($event:EventResponse): void {
    this.router.navigate(['book-event', $event.id]);
  }
}
