import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {CreateUpdateEventRequest} from "../model/request/event-save-update-request.model";
import {environment} from "../environments/environment";
import {EventResponse} from "../features/event/model/event-response.model";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) { }

  getEventById(id: string): Observable<EventResponse> {
    return this.http.get<EventResponse>(`${environment.baseUrl}/event/${id}`);
  }

  createEvent(createEventRequestDto: CreateUpdateEventRequest): Observable<EventResponse> {
    return this.http.post<EventResponse>(`${environment.baseUrl}/event`, createEventRequestDto);
  }

  updateEvent(id: string, createEventRequestDto: CreateUpdateEventRequest): Observable<EventResponse> {
    return this.http.put<EventResponse>(`${environment.baseUrl}/event/${id}`, createEventRequestDto);
  }

  deleteEvent(id: string): Observable<void> {
    return this.http.delete<void>(`${environment.baseUrl}/event/${id}`);
  }

  reserveEvent(id: string): Observable<EventResponse> {
    return this.http.put<EventResponse>(`${environment.baseUrl}/event/${id}/reserve`, {});
  }

  cancelReservation(id: string): Observable<EventResponse> {
    return this.http.put<EventResponse>(`${environment.baseUrl}/event/${id}/cancel-reservation`, {});
  }

  getOrganizerFinishedEvents(): Observable<Array<EventResponse>> {
    return this.http.get<Array<EventResponse>>(`${environment.baseUrl}/event/organizer-finished`);
  }

  getOrganizerPendingEvents(): Observable<Array<EventResponse>> {
    return this.http.get<Array<EventResponse>>(`${environment.baseUrl}/event/organizer-pending`);
  }

  getVisitedEvents(): Observable<Array<EventResponse>> {
    return this.http.get<Array<EventResponse>>(`${environment.baseUrl}/event/visitor-visited`);
  }

  getReservedEvents(): Observable<Array<EventResponse>> {
    return this.http.get<Array<EventResponse>>(`${environment.baseUrl}/event/visitor-reserved`);
  }

  getAllAvailableEvents(): Observable<Array<EventResponse>> {
    return this.http.get<Array<EventResponse>>(`${environment.baseUrl}/event/visitor-available`);
  }

  getRecommendedEvents(): Observable<Array<EventResponse>> {
    return this.http.get<Array<EventResponse>>(`${environment.baseUrl}/event/visitor-recommended`);
  }
}
