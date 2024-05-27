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
import {CountryDto} from "../../../model/response/country-dto.model";
import {CountryService} from "../../../service/country.service";
import {HttpErrorResponse} from "@angular/common/http";
import {CreateSpecialOfferRequest} from "../../../model/request/create-special-offer-request.model";

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.component.html',
  styleUrls: ['./event-form.component.scss'],
})
export class EventFormComponent implements OnInit {

  event: EventResponse = {} as EventResponse;
  @Input() formTitle: string = '';
  @Input() actionButton1Text: string = '';
  @Input() actionButton2Text: string = '';
  @Input() isFormReadOnly: boolean = false;
  @Input() isActionButton1Enabled: boolean = false;
  @Input() isActionButton2Enabled: boolean = false;

  eventName: string = '';
  startDateTime: Date | null = null;
  endDateTime: Date | null = null;
  price: number | null = null;
  seats: number | null = null;
  shortDescription: string | null = null;
  detailedDescription: string | null = null;
  organizationPlan: string | null = null;
  countries: Array<CountryDto> = [];
  selectedCountryId: string = "";

  @Output() button1ClickEmit = new EventEmitter();
  @Output() button2ClickEmit = new EventEmitter();


  constructor(private eventService: EventService,
              private route: ActivatedRoute,
              private toastrService: ToastrService,
              private countryService: CountryService,
              private router:Router) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    this.countryService.getAll().subscribe({
      next: response => {
        this.selectedCountryId = response[0].id;
        this.countries = response;
      }, error: error => {
        if (error instanceof HttpErrorResponse) {
          console.log(error.error.message);
        }
      }
    })

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
        this.seats = event.totalSeats;
        this.shortDescription = event.shortDescription;
        this.detailedDescription = event.detailedDescription;
        this.organizationPlan = event.organizationPlan;
        this.selectedEventType = event.type;

        this.countryService.getById(event.countryId).subscribe({
          next: response => {
            this.selectedCountryId = response.id;
          }, error: error => {
            if (error instanceof HttpErrorResponse) {
              console.log(error.error.message);
            }
          }
        })
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

    if (this.price === null) {
      this.toastrService.error('Please enter price');
      return;
    }

    if (this.seats === null) {
      this.toastrService.error('Please enter seats');
      return;
    }

    if (this.shortDescription === null) {
      this.toastrService.error('Please enter short description');
      return;
    }

    if (this.detailedDescription === null) {
      this.toastrService.error('Please enter detailed description');
      return;
    }

    if (this.organizationPlan === null) {
      this.toastrService.error('Please enter organization plan');
      return;
    }

    let specialOffer: CreateSpecialOfferRequest | undefined = undefined;
    if (this.specialOffer.type != this.specialOfferTypes[0]) {  // if it isn't NO_SPECIAL_OFFER
      specialOffer = {
        discount: this.specialOffer.discount / 100.0,
        type: this.specialOffer.type
      }
      if (specialOffer.discount <= 0 || specialOffer.discount >= 100) {
        this.toastrService.error("Special offer discount must be between 0 and 100!");
        return;
      }
    }

    let eventSaveUpdateRequest: CreateUpdateEventRequest = {
      name: this.eventName,
      startDateTime: this.startDateTime,
      endDateTime: this.endDateTime,
      price: this.price,
      totalSeats: this.seats,
      shortDescription: this.shortDescription,
      detailedDescription: this.detailedDescription,
      organizationPlan: this.organizationPlan,
      type: this.selectedEventType,
      countryId: this.selectedCountryId,
      specialOffer: specialOffer
    };
    console.log(eventSaveUpdateRequest)
    this.button1ClickEmit.emit(eventSaveUpdateRequest);
  }

  emitButton2Click(): void {
    this.button2ClickEmit.emit(this.event);
  }

  eventTypes: Array<string> = [
    "HIKING",
    "CYCLING",
    "PICNIC",
    "MUSEUM_VISIT",
    "GALLERY_VISIT",
    "CONCERT",
    "ZOO_VISIT",
    "AQUARIUM_VISIT",
    "THEME_PARK_VISIT",
    "BASKETBALL_GAME",
    "FOOTBALL_MATCH",
    "BOXING_MATCH",
    "WELLNESS_CENTER",
    "SPA_TREATMENT",
    "SPA_VISIT",
    "ART_LECTURE",
    "ART_WORKSHOP",
    "PARAGLIDING",
    "BALLOON_RIDE",
    "MICHELIN_STAR_RESTAURANT",
    "MULTIPLE_GENRE_CONCERT"
  ];


  specialOfferTypes: Array<string> = [
    "NO_SPECIAL_OFFER",
    "FOR_LOCALS",
  ];

  specialOffer: CreateSpecialOfferRequest = {
    discount: 0.0,
    type: this.specialOfferTypes[0]
  }
  selectedEventType: string = this.eventTypes[0];

  formatEnum(preference: string) {
    return preference.replaceAll("_", " ");
  }

}
