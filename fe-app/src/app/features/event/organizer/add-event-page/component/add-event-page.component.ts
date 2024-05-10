import { Component, Input, OnInit } from '@angular/core';
import { CreateUpdateEventRequest } from 'src/app/model/request/event-save-update-request.model';
import {EventService} from "../../../../../service/event.service";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-add-event-page',
  templateUrl: './add-event-page.component.html',
  styleUrls: ['./add-event-page.component.scss'],
})
export class AddEventPageComponent implements OnInit {
  constructor(private eventService:EventService,
              private router:Router,
              private toastrService: ToastrService) {}

  ngOnInit(): void {}

  onAddNewFormAction($event: CreateUpdateEventRequest) {
    console.log($event);
    this.eventService.createEvent($event).subscribe((_) => {
      this.toastrService.success('Event created successfully');
      this.router.navigate(['/event']);
    }, (error:any) => {
      this.toastrService.error(error.error.message);
    })
  }
}
