import { Component, signal } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { ExcelReaderComponent } from './excel-reader-component/excel-reader-component';

@Component({
  selector: 'app-root',
  imports: [MatTabsModule, ExcelReaderComponent],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('frontend');
}
