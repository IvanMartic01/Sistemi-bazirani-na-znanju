import { Component, OnInit } from '@angular/core';
import {EventResponse} from "../../model/event-response.model";
import {EventService} from "../../../../service/event.service";
import {ActivatedRoute} from "@angular/router";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-buking-event-page',
  templateUrl: './booking-event-page.component.html',
  styleUrls: ['./booking-event-page.component.scss']
})
export class BookingEventPageComponent implements OnInit {

  reservedEvents:Array<EventResponse> = [];
  event:EventResponse = {} as EventResponse;
  isReserved:boolean = false;

  constructor(private eventService:EventService,
              private route: ActivatedRoute,
              private toastrService:ToastrService) { }


  async ngOnInit(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.toastrService.error('Event id is not provided');
      return;
    }
    await this.eventService.getEventById(id).toPromise()
      .then(event => this.event = event!)
      .catch(error => this.toastrService.error(error.error.message));

    await this.eventService.getReservedEvents().toPromise()
      .then(events => this.reservedEvents = events!);

    this.isReserved = this.reservedEvents
      .some(event => event.id === this.event.id);
  }

  reserve() {
    this.eventService.reserveEvent(this.event.id).subscribe(
      () => {
        this.isReserved = true;
        this.toastrService.success('Event reserved successfully');
      },
      error => this.toastrService.error(error.error.message));
  }

  cancelReservation() {
    this.eventService.cancelReservation(this.event.id).subscribe(
      () => {
        this.isReserved = false;
        this.toastrService.success('Reservation canceled successfully');
      },
      error => this.toastrService.error(error.error.message)
    );
  }
}
