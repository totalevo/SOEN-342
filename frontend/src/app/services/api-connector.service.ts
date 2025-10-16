import { Injectable } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { SearchParameters } from '../models/SearchParameters.model';
import { TripDTO } from "../models/TripDTO.model";

@Injectable({
  providedIn: 'root',
})
export class ApiConnectorService {
  private apiUrl = 'http://localhost:8080/';

  private resultsSubject = new BehaviorSubject<any[]>([]);
  results$ = this.resultsSubject.asObservable();

  constructor(private http: HttpClient) {}

  saveConnectionListFromExcel(connections: Connection[]): Observable<any> {
    return this.http.post(this.apiUrl + 'api/connections/upload', connections);
  }
  searchForConnections(searchParameters: SearchParameters): Observable<any[]> {
    return this.http.post<any[]>(this.apiUrl + 'api/connections/search', searchParameters);
  }
  searchIndirectConnections(searchParameters: SearchParameters): Observable<any[]> {
    return this.http.post<any[]>(this.apiUrl + 'api/connections/indirect', searchParameters);
  }
  searchTripsByTraveller(travellerId: string, lastName: string): Observable<TripDTO[]> {
    return this.http.post<TripDTO[]>(this.apiUrl + 'api/trips/search', {
      id: travellerId,
      name: lastName
    });
  }
}