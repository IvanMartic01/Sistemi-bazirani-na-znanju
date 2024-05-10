import {
  AfterViewInit,
  Component,
  EventEmitter,
  Input, OnInit,
  Output,
  ViewChild,
} from '@angular/core';

import { CreateUpdateEventRequest } from 'src/app/model/request/event-save-update-request.model';
import {ToastrService} from "ngx-toastr";
import {EventService} from "../../../service/event.service";
import {ActivatedRoute, Router} from "@angular/router";
import {EventResponse} from "../model/event-response.model";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss'],
})
export class EventFormComponent implements OnInit {

  event:EventResponse = {} as EventResponse;
  @Input() formTitle: string = '';
  @Input() actionButton1Text: string = '';
  @Input() actionButton2Text: string = '';
  @Input() isFormReadOnly: boolean = false;
  @Input() isActionButton1Enabled: boolean = false;
  @Input() isActionButton2Enabled: boolean = false;

  eventImage: File = {} as File;
  imgURL: any;
  eventName: string = '';
  startDateTime: Date | null = null;
  endDateTime: Date | null = null;
  price: number = 0;
  seats: number = 0;
  shortDescription = '';
  detailedDescription: string = '';
  organizationPlan: string = '';

  @Output() button1ClickEmit = new EventEmitter();
  @Output() button2ClickEmit = new EventEmitter();


  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private toastrService: ToastrService,
              private router:Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      return;
    }

    this.eventService.getEventById(id).subscribe(
      event => {
        this.event = event;

        this.eventName = event.name;
        this.startDateTime = new Date(event.startDateTime);
        this.endDateTime = new Date(event.endDateTime);
        this.price = event.price;
        this.seats = event.seats;
        this.shortDescription = event.shortDescription;
        this.detailedDescription = event.detailedDescription;
        this.organizationPlan = event.organizationPlan;
      },
      error => {
        this.toastrService.error(error.error.message)
        this.router.navigate(['/organizer-events'])
      }
    )
  }

  emitButton1Click(): void {
    if (this.startDateTime === null || this.endDateTime === null) {
      this.toastrService.error('Please select start and end date');
      return;
    }

    let eventSaveUpdateRequest: CreateUpdateEventRequest = {
      name: this.eventName,
      startDateTime: this.startDateTime,
      endDateTime: this.endDateTime,
      price: this.price,
      totalSeats: this.seats,
      shortDescription: this.shortDescription,
      detailedDescription: this.detailedDescription,
      organizationPlan: this.eventName,
    };
    console.log(eventSaveUpdateRequest)
    this.button1ClickEmit.emit({ "data":eventSaveUpdateRequest, "image":this.eventImage});
  }

  emitButton2Click(): void {
    this.button2ClickEmit.emit(this.event);
  }
}
