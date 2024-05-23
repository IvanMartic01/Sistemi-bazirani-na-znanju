import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CountryDto} from "../model/response/country-dto.model";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class CountryService {

  constructor(
    private http: HttpClient
  ) { }

  public getAll(): Observable<Array<CountryDto>> {
    return this.http.get<Array<CountryDto>>(`${environment.baseUrl}/country/all`);
  }

  public getById(id: string): Observable<CountryDto> {
    return this.http.get<CountryDto>(`${environment.baseUrl}/country/${id}`);
  }
}
