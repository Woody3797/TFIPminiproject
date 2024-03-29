import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './components/main/main.component';
import { ProductlistComponent } from './components/productlist/productlist.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ProductaddComponent } from './components/productadd/productadd.component';
import { ProductComponent } from './components/product/product.component';
import { ProducteditComponent } from './components/productedit/productedit.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './auth.guard';
import { ProductlistallComponent } from './components/productlistall/productlistall.component';
import { ChatComponent } from './components/chat/chat.component';
import { ForgotpasswordComponent } from './components/forgotpassword/forgotpassword.component';
import { WatchlistComponent } from './components/watchlist/watchlist.component';
import { ChatroomComponent } from './components/chatroom/chatroom.component';

const routes: Routes = [
    {path: '', component: MainComponent, title: 'Welcome', canActivate: [authGuard]},
    {path: 'login', component: LoginComponent, title: 'Login/Sign-up'},
    {path: 'forgotpassword', component: ForgotpasswordComponent, title: 'Password Reset'},
    {path: ':email/productlist', component: ProductlistComponent, title: 'Products', canActivate: [authGuard]},
    {path: ':email/watchlist', component: WatchlistComponent, title: 'Product Watchlist', canActivate: [authGuard]},
    {path: ':email/profile', component: ProfileComponent, title: 'Profile', canActivate: [authGuard]},
    {path: ':email/addproduct', component: ProductaddComponent, title: 'Add Product', canActivate: [authGuard]},
    {path: 'product/:productID', component: ProductComponent, title: 'Product', canActivate: [authGuard]},
    {path: 'editproduct/:productID', component: ProducteditComponent, title: 'Edit Product', canActivate: [authGuard]},
    {path: ':email/allproducts', component: ProductlistallComponent, title: 'All Products', canActivate: [authGuard]},
    {path: ':email/chat', component: ChatComponent, title: 'Chat', canActivate: [authGuard]},
    {path: 'chat', component: ChatroomComponent, title: 'Chat', canActivate: [authGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true, onSameUrlNavigation: 'reload'})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
