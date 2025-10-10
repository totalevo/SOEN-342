import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IndirectResultContext } from '../models/IndirectResultContext.model';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
@Component({
  selector: 'app-indirect-result-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './indirect-result-component.html',
  styleUrl: './indirect-result-component.css'
})
export class IndirectResultComponent implements OnInit{
  @Input() journeys: IndirectResultContext[][] = [];
  show2Legs = true;
  show3Legs = true;

  ngOnInit() {
    console.log(this.journeys);
  }
}
