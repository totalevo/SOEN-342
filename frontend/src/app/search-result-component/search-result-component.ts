import { Component, OnInit, Input } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { CommonModule } from '@angular/common';
import { ApiConnectorService } from '../services/api-connector.service';
import { Router } from '@angular/router';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-search-result-component',
  styleUrls: ['./search-result-component.css'],
  imports: [CommonModule],
  standalone: true,
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
        <button mat-raised-button color="primary" (click)="goToBooking(result)">
          Go to Booking
        </button>
        <hr>
      </ng-container>
    </div>
  `
})
export class SearchResultComponent implements OnInit {
  @Input() results: Connection[] = [];
  constructor(
    private apiConnectorService: ApiConnectorService,
    private router: Router,
    private bookingService: BookingService,
) {}
    
  ngOnInit() {
    // this.apiConnectorService.results$.subscribe(data => {
    //   this.results = data;
    // });
  }
  trackByConnectionId(index: number, item: Connection) {
    return item.connectionId;
  }

  goToBooking(connection: Connection) {
    this.bookingService.setConnections([connection]);
    this.router.navigate(['booking']);
  }
}
