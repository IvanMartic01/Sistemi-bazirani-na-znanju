import { Component, OnInit } from '@angular/core';
import {EventService} from "../../../../../service/event.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {CreateUpdateEventRequest} from "../../../../../model/request/event-save-update-request.model";
import {EventResponse} from "../../../model/event-response.model";

@Component({
  selector: 'app-edit-event-page',
  templateUrl: './edit-event-page.component.html',
  styleUrls: ['./edit-event-page.component.scss']
})
export class EditEventPageComponent implements OnInit {

  id: string = '';
  constructor(private eventService:EventService,
              private route:ActivatedRoute,
              private router:Router,
              private toastrService: ToastrService) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.toastrService.error('Event id is not provided');
      return;
    }
    this.id = id;
  }

  onUpdate($event: CreateUpdateEventRequest) {
    this.eventService.updateEvent(this.id, $event).subscribe((_) => {
      this.toastrService.success('Event updated successfully');
      this.router.navigate(['/pending-events']);
    }, (error:any) => {
      this.toastrService.error(error.error.message);
    })
  }

  onDelete($event: EventResponse) {
    this.eventService.deleteEvent($event.id).subscribe(
      () => {
        this.toastrService.success('Event deleted successfully')
        this.router.navigate(['/pending-events']);
      },
      error => this.toastrService.error(error.error.message)
    )
  }
}
