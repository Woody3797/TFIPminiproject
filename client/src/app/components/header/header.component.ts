import { SocialAuthService } from '@abacritt/angularx-social-login';
import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

    router = inject(Router)
    productService = inject(ProductService)
    loginService = inject(LoginService)
    storageService = inject(StorageService)
    authService = inject(SocialAuthService)

    email = ''
    isLoggedIn = false


    ngOnInit(): void {
        this.isLoggedIn = this.storageService.isLoggedIn()
        this.email = this.storageService.isLoggedIn() ? this.storageService.getUser().email : ''
    }

    gotoProfile() {
        this.router.navigate([this.email + '/profile'])
    }

    addProduct() {
        this.router.navigate([this.email + '/addproduct'])
    }

    listProducts() {
        this.router.navigate([ this.email + '/productlist'])
    }

    listAllProducts() {
        this.router.navigate([ this.email + '/allproducts'], { queryParams: {
            pageSize: 10,
            pageIndex: 0
        } })
    }

    logout() {
        this.email = ''
        this.storageService.clearStorage()
        this.loginService.logout().subscribe()
        this.authService.signOut(true).then(response => {
            console.info(response)
        }).catch(err => {
            console.info(err)
        })
        this.router.navigate(['/login'])
    }
}
