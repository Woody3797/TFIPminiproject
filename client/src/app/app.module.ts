import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './modules/material.module';
import { MainComponent } from './components/main/main.component';
import { ProductlistComponent } from './components/productlist/productlist.component';
import { ProductaddComponent } from './components/productadd/productadd.component';
import { ProfileComponent } from './components/profile/profile.component';
import { HeaderComponent } from './components/header/header.component';
import { ProductComponent } from './components/product/product.component';
import { ProducteditComponent } from './components/productedit/productedit.component';
import { LoginComponent } from './components/login/login.component';
import { httpInterceptorProviders } from './http.interceptor';
import { SocialLoginModule } from '@abacritt/angularx-social-login';
import { SocialAuthServiceProvider } from './modules/social.module';
import { GoogleSigninButtonModule } from '@abacritt/angularx-social-login';
import { ProductlistallComponent } from './components/productlistall/productlistall.component';


@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    ProductlistComponent,
    ProductaddComponent,
    ProfileComponent,
    HeaderComponent,
    ProductComponent,
    ProducteditComponent,
    LoginComponent,
    ProductlistallComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule,
    SocialLoginModule,
    GoogleSigninButtonModule,
  ],
  providers: [
    httpInterceptorProviders,
    SocialAuthServiceProvider,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
