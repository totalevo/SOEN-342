import { Injectable } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { SearchParameters } from '../models/SearchParameters.model';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Traveller } from '../models/Traveller.model';
import { TripDTO } from '../models/TripDTO.model';
@Injectable({
  providedIn: 'root',
})
export class BookingService {
  private apiUrl = 'http://localhost:8080/';

  private resultsSubject = new BehaviorSubject<Connection[]>([]);
  results$ = this.resultsSubject.asObservable();

  travellers: Traveller[] = [];

  constructor(private http: HttpClient) {}

  bookTrip(): Observable<any> {
    const trip = {
      connections: this.resultsSubject.getValue(),
      travellers: this.travellers
    }
    return this.http.post(this.apiUrl + 'api/trip', trip, { responseType: 'text' });
  }
  setConnections(connections: Connection[]) {
    this.resultsSubject.next(connections);
  }

  getConnections(): Connection[] {
    return this.resultsSubject.value;
  }

  addTraveller(traveller: Traveller) {
    this.travellers.push(traveller);
  }
}