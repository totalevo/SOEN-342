import { Component, Input  } from '@angular/core';
import { Connection } from '../models/Connection.model';
import { Traveller } from '../models/Traveller.model';
import { TripDTO } from '../models/TripDTO.model';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { OnInit } from '@angular/core';
import { BookingService } from '../services/booking.service';
import { MatTabsModule } from '@angular/material/tabs';
import { TravellerComponent } from '../traveller-component/traveller-component';
@Component({
  selector: 'app-booking-component',
  imports: [MatTabsModule, TravellerComponent],
  templateUrl: './booking-component.html',
  styleUrl: './booking-component.css',
})
export class BookingComponent  implements OnInit {
  connections: Connection[] = [];
  travellers: Traveller[] = [];
  constructor(
    private bookingService: BookingService,
    private router: Router,
  ) {}
  
  ngOnInit() {
    this.connections = this.bookingService.getConnections();
  }

  onAddTab(traveller: Traveller) {
    this.travellers.push(traveller);
  }

  bookTrip() {
    this.bookingService.bookTrip().subscribe({
      next: (response) => {
        alert('Trip booked successfully!');
        console.log('Response:', response);
      },
      error: (err) => {
        console.error('Booking failed:', err);
        const error = err.error;
        alert('Failed to book trip. Please try again.' + error);
      },
    });
    this.bookingService.travellers = [];
    this.router.navigate(['']);
  }
}
