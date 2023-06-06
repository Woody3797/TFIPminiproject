import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main/main.component';
import { ProductlistComponent } from './components/productlist/productlist.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ProductaddComponent } from './components/productadd/productadd.component';

const routes: Routes = [
    {path: '', component: MainComponent, title: 'Welcome'},
    {path: 'productlist', component: ProductlistComponent, title: 'Products'},
    {path: 'addproduct', component: ProductaddComponent, title: 'Add'},
    {path: 'profile', component: ProfileComponent, title: 'Profile'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
