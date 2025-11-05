import { Component, EventEmitter, Output } from '@angular/core';
import { Traveller } from '../models/Traveller.model';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-traveller-component',
  imports: [ReactiveFormsModule],
  templateUrl: './traveller-component.html',
  styleUrl: './traveller-component.css'
})
export class TravellerComponent {
  @Output() travellerSaved = new EventEmitter<false>();
  name = new FormControl('');
  age = new FormControl(null);
  id = new FormControl('');
  traveller: Traveller = { id: '', name: '', age: 0 };
  saved: Boolean = false;

  constructor(
    private bookingService: BookingService
  ) { }

  saveTraveller() {
    this.traveller.name = this.name.value ?? '';
    this.traveller.age = this.age.value ?? 0;
    this.traveller.id = this.id.value ?? '';

    if (!this.saved) {
      
      this.bookingService.addTraveller(this.traveller);
      this.saved = true;
    }
    
    console.log(this.bookingService.travellers);

    this.travellerSaved.emit(false);
  }
}
