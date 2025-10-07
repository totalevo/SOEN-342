import { Component, OnInit } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { CommonModule } from '@angular/common';
import { ApiConnectorService } from '../services/api-connector.service';

@Component({
  selector: 'app-search-result-component',
  styleUrls: ['./search-result-component.css'],
  imports: [CommonModule],
  template: `
    <h3>Search Results</h3>
    <div *ngIf="results.length === 0">
      No connections found.
    </div>
    <div *ngIf="results.length > 0" class="result-item">
      <ng-container *ngFor="let result of results; let i = index; trackBy: trackByConnectionId">
        <h4>Result {{ i + 1 }}</h4>
        <p><strong>Arrival City:</strong> {{ result.arrivalCity }}</p>
        <p><strong>Departure City:</strong> {{ result.departureCity }}</p>
        <p><strong>Departure Time:</strong> {{ result.departureTime }}</p>
        <p><strong>Arrival Time:</strong> {{ result.arrivalTime }}</p>
        <p><strong>Train Type:</strong> {{ result.trainType }}</p>
        <p><strong>Days of Operation:</strong> {{ result.daysOfOperation }}</p>
        <p><strong>First Class Rate:</strong> {{ result.firstClassRate }}</p>
        <p><strong>Second Class Rate:</strong> {{ result.secondClassRate }}</p>
        <p><strong>Duration (Minutes):</strong> {{ result.durationMinutes }}</p>
        <hr>
      </ng-container>
    </div>
  `
})
export class SearchResultComponent implements OnInit {
  results: Connection[] = [];
  constructor(private apiConnectorService: ApiConnectorService) {}
  ngOnInit() {
    this.apiConnectorService.results$.subscribe(data => {
      this.results = data;
    });
  }
  trackByConnectionId(index: number, item: Connection) {
    return item.connectionId;
  }
}
