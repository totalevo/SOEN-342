import { Injectable } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { SearchParameters } from '../models/SearchParameters.model';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
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
  searchForConnections(searchParameters: SearchParameters): void {
    this.http.post<any[]>(this.apiUrl + 'api/connections/search', searchParameters)
      .subscribe(results => {
        this.resultsSubject.next(results);
      });
  }
  searchIndirectConnections(searchParameters: SearchParameters): void {
    this.http.post<any[]>(this.apiUrl + 'api/connections/indirect', searchParameters)
      .subscribe(results => {
        this.resultsSubject.next(results);
      });
  }
}