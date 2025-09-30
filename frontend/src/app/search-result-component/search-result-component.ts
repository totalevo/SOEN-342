import { Component, OnInit } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { ApiConnectorService } from '../services/api-connector.service';

@Component({
  selector: 'app-search-result-component',
  imports: [],
  styleUrl: './search-result-component.css',
  template:`

  <h3>Search Results</h3>
  <div class="result-item">
    @for (result of results; track result.connectionId; let i = $index) {
        <h4>Result {{ i + 1 }}</h4>
        <p><strong>Arrival City:</strong> {{ result.arrivalCity }}</p>
        <p><strong>Departure City:</strong> {{ result.departureCity }}</p>
        <p><strong>Departure Time:</strong> {{ result.departureTime }}</p>
        <p><strong>Arrival Time:</strong> {{ result.arrivalTime }}</p>
        <p><strong>Train Type:</strong> {{ result.trainType }}</p>
        <p><strong>Days of Operation:</strong> {{ result.daysOfOperation }}</p>
        <p><strong>First Class Rate:</strong> {{ result.firstClassRate }}</p>
        <p><strong>Second Class Rate:</strong> {{ result.secondClassRate }}</p>
        <hr>
    }  
  </div>
  `,
})
export class SearchResultComponent implements OnInit{
  results: any[] = [];
  constructor(private apiConnectorService: ApiConnectorService) {}
  ngOnInit() {
    this.apiConnectorService.results$.subscribe(data => {
      this.results = data;
      console.log(this.results);
    })
  }
}
