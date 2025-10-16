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
  @Output() addTab = new EventEmitter<Traveller>();
  name = new FormControl('');
  age = new FormControl(null);
  id = new FormControl('');
  traveller: Traveller;

  saved: Boolean = false;

  constructor(
    private bookingService: BookingService
  ) {
    this.traveller = {
      age: this.age.value ?? 0,
      id: this.id.value ?? '',
      name: this.name.value ?? ''
    }
  }

  createNewTab() {
    const traveller = { id: 'aa', name: 'aaaa', age: 12 };
    this.addTab.emit(traveller);
  }

  saveTraveller() {
    this.traveller = {
        age: this.age.value ?? 0,
        id: this.id.value ?? '',
        name: this.name.value ?? ''
      }
    if (!this.saved) {
      
      this.bookingService.addTraveller(this.traveller);
      this.saved = true;
    }
    
      console.log(this.bookingService.travellers);
  }
}
