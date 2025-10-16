import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-view-trips-component',
  imports: [CommonModule, MatCardModule],
  templateUrl: './view-trips-component.html',
  styleUrl: './view-trips-component.css',
  standalone: true
})
export class ViewTripsComponent {

}
