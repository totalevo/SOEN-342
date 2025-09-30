import { Routes } from '@angular/router';
import { ExcelReaderComponent } from './excel-reader-component/excel-reader-component';
import { SearchComponent } from './search-component/search-component';
import { SearchPageComponent } from './search-page-component/search-page-component';

export const routes: Routes = [{ path: 'upload', component: ExcelReaderComponent }, {path: 'search', component: SearchPageComponent}];
