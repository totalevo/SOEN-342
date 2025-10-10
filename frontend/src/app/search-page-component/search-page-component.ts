import { Component } from '@angular/core';
import { SearchComponent } from '../search-component/search-component';
import { SearchResultComponent } from '../search-result-component/search-result-component';

@Component({
  selector: 'app-search-page-component',
  imports: [SearchComponent],
  templateUrl: './search-page-component.html',
  styleUrl: './search-page-component.css'
})
export class SearchPageComponent {

}
