import { Injectable } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class ApiConnectorService {
  private apiUrl = 'http://localhost:8080/';

  constructor(private http: HttpClient) {}

  saveConnectionListFromExcel(connections: Connection[]): Observable<any> {
    return this.http.post(this.apiUrl + 'api/connections/upload', connections);
  }
}
