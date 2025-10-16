import { Routes } from '@angular/router';
import { BookingComponent } from './booking-component/booking-component';
import { MenuComponent } from './menu-component/menu-component';
export const routes: Routes = [
    { path:'booking', component: BookingComponent},
    { path:'', component: MenuComponent}
];
