import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { SearchParameters } from '../models/SearchParameters.model';
import { ApiConnectorService } from '../services/api-connector.service';
import { SearchResultComponent } from '../search-result-component/search-result-component';
import { IndirectResultComponent } from '../indirect-result-component/indirect-result-component';
import { firstValueFrom } from 'rxjs';


@Component({
  selector: 'app-search-component',
  imports: [ReactiveFormsModule, IndirectResultComponent, CommonModule, SearchResultComponent],
  templateUrl:  './search-component.html',
  styleUrl: './search-component.css'
})
export class SearchComponent {
  searchParameters!: SearchParameters;
  directResults: any[] = [];
  indirectRoutes: any[][] = [];
  showDirect: boolean = true;
  showIndirect: boolean = false;
  constructor(private apiConnectorService: ApiConnectorService) {}

  DAY_MASKS = {
  monday: 1 << 6,    // 64
  tuesday: 1 << 5,   // 32
  wednesday: 1 << 4, // 16
  thursday: 1 << 3,  // 8
  friday: 1 << 2,    // 4
  saturday: 1 << 1,  // 2
  sunday: 1 << 0     // 1
  };

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
    monday: new FormControl(false),
    tuesday: new FormControl(false),
    wednesday: new FormControl(false),
    thursday: new FormControl(false),
    friday: new FormControl(false),
    saturday: new FormControl(false),
    sunday: new FormControl(false),
    duration: new FormControl(''),
  });

  async submitApplication() {
  this.searchParameters = {
    connectionId: null,
    arrivalCity: this.applyForm.value.arrivalCity || null,
    departureCity: this.applyForm.value.departureCity || null,
    departureTime: this.applyForm.value.departureTime || null,
    arrivalTime: this.applyForm.value.arrivalTime || null,
    trainType: this.applyForm.value.trainType || null,
    firstClassRate: Number(this.applyForm.value.firstClassRate) || null,
    secondClassRate: Number(this.applyForm.value.secondClassRate) || null,
    sortBy: this.applyForm.value.sortBy || null,
    sortOrder: this.applyForm.value.sortOrder || null,
    bitmapDays: this.buildDaysBitmap(),
    duration: Number(this.applyForm.value.duration) || null,
  };

  this.showDirect = false;
  this.showIndirect = false;
  this.directResults = [];
  this.indirectRoutes = [];

  // Direct API call
  const directResults = await firstValueFrom(this.apiConnectorService.searchForConnections(this.searchParameters));
  if (Array.isArray(directResults) && directResults.length > 0) {
    this.directResults = directResults;
    this.showDirect = true;
    this.showIndirect = false;
    return; // Only display direct, do not attempt indirect
  }

  // Indirect API call only if both cities present and direct not found
  if (this.searchParameters.departureCity && this.searchParameters.arrivalCity) {
    const indirectResults = await firstValueFrom(this.apiConnectorService.searchIndirectConnections(this.searchParameters));
    
    // DEBUG

    if (Array.isArray(indirectResults) && indirectResults.length > 0 && Array.isArray(indirectResults[0])) {
      this.indirectRoutes = indirectResults;
      this.showDirect = false;
      this.showIndirect = true; 
    } else {
      this.indirectRoutes = [];
      this.showDirect = false;
      this.showIndirect = true;
    }
  } else {
    this.directResults = [];
    this.showDirect = false;
    this.indirectRoutes = [];
    this.showIndirect = false;
  }
}

  buildDaysBitmap(): number {
    let bitmap = 0;
    for (const day of Object.keys(this.DAY_MASKS) as Array<keyof typeof this.DAY_MASKS>) {
      if (this.applyForm.get(day)?.value) {
        bitmap |= this.DAY_MASKS[day];
      }
    }
    return bitmap;
  }
}
