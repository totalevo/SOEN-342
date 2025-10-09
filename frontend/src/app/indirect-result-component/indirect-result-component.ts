import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Connection } from '../models/Connection.model';

@Component({
  selector: 'app-indirect-result-component',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './indirect-result-component.html',
  styleUrl: './indirect-result-component.css'
})
export class IndirectResultComponent {
  @Input() journeys: Connection[][] = [];
}
