import { Component, signal } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { RouterModule } from '@angular/router';
import { ExcelReaderComponent } from './excel-reader-component/excel-reader-component';
import { SearchPageComponent } from './search-page-component/search-page-component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [MatTabsModule, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
  constructor(private router: Router) {}

}
