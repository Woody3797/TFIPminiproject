import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main/main.component';
import { ProductlistComponent } from './components/productlist/productlist.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ProductaddComponent } from './components/productadd/productadd.component';
import { ProductComponent } from './components/product/product.component';
import { ProducteditComponent } from './components/productedit/productedit.component';
import { LoginComponent } from './components/login/login.component';

const routes: Routes = [
    {path: '', component: MainComponent, title: 'Welcome'},
    {path: 'login', component: LoginComponent, title: 'Login/Sign-up'},
    {path: ':username/productlist', component: ProductlistComponent, title: 'Products'},
    {path: ':username/addproduct', component: ProductaddComponent, title: 'Add Product'},
    {path: 'product/:productID', component: ProductComponent, title: 'Product'},
    {path: 'editproduct/:productID', component: ProducteditComponent, title: 'Edit Product'},
    {path: ':username/profile', component: ProfileComponent, title: 'Profile'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: false})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
