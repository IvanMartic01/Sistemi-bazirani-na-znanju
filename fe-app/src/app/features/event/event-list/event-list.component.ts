import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {EventResponse} from "../model/event-response.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-event-list',
  templateUrl: './event-list.component.html',
  styleUrls: ['./event-list.component.scss']
})
export class EventListComponent implements OnInit {

  @Input() events: Array<EventResponse> = [];
  @Output() onCardClickEmitter:EventEmitter<EventResponse> = new EventEmitter<EventResponse>();
  constructor() { }

  ngOnInit(): void {
  }

  onCardClick(event: EventResponse): void {
    this.onCardClickEmitter.emit(event);
  }

  formatEventType(eventType: string) {
    return eventType.replace("_", " ");
  }
}
