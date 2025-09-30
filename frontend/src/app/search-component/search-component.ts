import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SearchParameters } from '../models/SearchParameters.model';
import { ApiConnectorService } from '../services/api-connector.service';

@Component({
  selector: 'app-search-component',
  imports: [ReactiveFormsModule],
  templateUrl: './search-component.html',
  styleUrl: './search-component.css'
})
export class SearchComponent {
  searchParameters!: SearchParameters;
  constructor(private apiConnectorService: ApiConnectorService) {}
  applyForm = new FormGroup({
    arrivalCity: new FormControl(''),
    departureCity: new FormControl(''),
    arrivalTime: new FormControl(''),
    departureTime: new FormControl(''),
    trainType: new FormControl(''),
    daysOfOperation: new FormControl(''),
    firstClassRate: new FormControl(''),
    secondClassRate: new FormControl(''),
    sortBy: new FormControl(''),
    sortOrder: new FormControl(''),
  });

  submitApplication() {
    this.searchParameters = {
      connectionId: null,
      arrivalCity: this.applyForm.value.arrivalCity || null,
      departureCity: this.applyForm.value.departureCity || null,
      departureTime: this.applyForm.value.departureTime || null,
      arrivalTime: this.applyForm.value.arrivalTime || null,
      trainType: this.applyForm.value.trainType || null,
      daysOfOperation: this.applyForm.value.daysOfOperation || null,
      firstClassRate: Number(this.applyForm.value.firstClassRate) || null,
      secondClassRate: Number(this.applyForm.value.secondClassRate) || null,
      sortBy: this.applyForm.value.sortBy || null,
      sortOrder: this.applyForm.value.sortOrder || null,
    };
    console.log(this.searchParameters);
    this.apiConnectorService.searchForConnections(this.searchParameters);
  }
}
