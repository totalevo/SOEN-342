import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { TripDTO } from '../models/TripDTO.model';
import { ApiConnectorService } from '../services/api-connector.service';

@Component({
  selector: 'app-view-trips-component',
  imports: [CommonModule, ReactiveFormsModule, MatCardModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './view-trips-component.html',
  styleUrl: './view-trips-component.css',
  standalone: true
})
export class ViewTripsComponent {
  form = new FormGroup({
    lastName: new FormControl<string>('', Validators.required),
    travellerId: new FormControl<string>('', Validators.required),
  });

  showError = false;
  loading = false;
  serverError: string | null = null;
  trips: TripDTO[] = [];

  constructor(private api: ApiConnectorService){}

  onSearch(): void {
    if (this.form.invalid) {
      this.showError = true;
      this.form.markAllAsTouched(); // Highlights the "bad" fields, pretty cool
      return;
    }

    this.showError = false;
    this.serverError = null;
    this.trips = [];
    this.loading = true;

    const lastName = this.form.value.lastName!.trim(); // In case of any extra spaces typos or something
    const travellerId = this.form.value.travellerId!.trim();
    // DEBUG: Check whats being sent to the backend
    console.log('Searching trips for:',this.form.value);

    this.api.searchTripsByTraveller(travellerId, lastName).subscribe({
      next: (data) => {
        console.log('Trips response:', data);
        this.trips = Array.isArray(data) ? data : [];
        this.loading = false;
      },
      error: (err) => {
        this.serverError = "Could not load trips. Check if you have any trips related to this traveller name and id"
        console.error('searchTripsByTraveller error:', err);
        this.loading = false;
      },
    });
  }

  endTrip(t: TripDTO): void {
  if (t.tripStatus === 'COMPLETED') return;

  this.api.completeTrip(t.tripId).subscribe({
    next: (updated) => {
      // reflect the change locally
      t.tripStatus = updated.tripStatus;
    },
    error: (err) => {
      console.error('completeTrip error:', err);
      this.serverError = 'Failed to end the trip. Please try again.';
    }
    });
  }

  get currentTrips(): TripDTO[] {
    return this.trips.filter(t => t.tripStatus === 'RESERVED');
  }

  get historyTrips(): TripDTO[] {
    return this.trips.filter(t => t.tripStatus === 'COMPLETED');
  }


}
