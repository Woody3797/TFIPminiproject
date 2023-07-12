import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { GoogleSigninButtonModule, SocialLoginModule } from '@abacritt/angularx-social-login';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChatComponent } from './components/chat/chat.component';
import { ForgotpasswordComponent } from './components/forgotpassword/forgotpassword.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { MainComponent } from './components/main/main.component';
import { ProductComponent } from './components/product/product.component';
import { ProductaddComponent } from './components/productadd/productadd.component';
import { ProducteditComponent } from './components/productedit/productedit.component';
import { ProductlistComponent } from './components/productlist/productlist.component';
import { ProductlistallComponent } from './components/productlistall/productlistall.component';
import { ProducttagsComponent } from './components/producttags/producttags.component';
import { ProfileComponent } from './components/profile/profile.component';
import { httpInterceptorProviders } from './http.interceptor';
import { MaterialModule } from './modules/material.module';
import { SocialAuthServiceProvider } from './modules/social.module';


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
    ProducttagsComponent,
    ChatComponent,
    ForgotpasswordComponent,
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
