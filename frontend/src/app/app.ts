import { Component, signal } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { ExcelReaderComponent } from './excel-reader-component/excel-reader-component';
import { SearchPageComponent } from './search-page-component/search-page-component';

@Component({
  selector: 'app-root',
  imports: [MatTabsModule, ExcelReaderComponent, SearchPageComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
}
