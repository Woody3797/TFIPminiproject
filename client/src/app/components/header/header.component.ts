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

    email = ''
    loggedIn = this.storageService.isLoggedIn()


    ngOnInit(): void {
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

    logout() {
        this.email = ''
        this.storageService.clearStorage()
        this.loginService.logout().subscribe()
        this.router.navigate(['/'])
    }
}
