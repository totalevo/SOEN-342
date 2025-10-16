import { Component } from '@angular/core';
import { MatTabsModule } from '@angular/material/tabs';
import { RouterModule } from '@angular/router';
import { ExcelReaderComponent } from '../excel-reader-component/excel-reader-component';
import { SearchPageComponent } from '../search-page-component/search-page-component';
import { ViewTripsComponent } from '../view-trips-component/view-trips-component';
@Component({
  selector: 'app-menu-component',
  imports: [MatTabsModule, RouterModule, ExcelReaderComponent, SearchPageComponent, ViewTripsComponent],
  templateUrl: './menu-component.html',
  styleUrl: './menu-component.css'
})
export class MenuComponent {

}
