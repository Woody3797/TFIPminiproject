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

    username = ''
    loggedIn = this.storageService.isLoggedIn()


    ngOnInit(): void {
        this.username = this.storageService.isLoggedIn() ? this.storageService.getUser().username : ''
    }

    gotoProfile() {
        this.router.navigate([this.username + '/profile'])
    }

    addProduct() {
        this.router.navigate([this.username + '/addproduct'])
    }

    listProducts() {
        this.router.navigate([ this.username + '/productlist'])
    }

    logout() {
        this.username = ''
        this.storageService.clearStorage()
        this.loginService.logout().subscribe()
        this.router.navigate(['/'])
    }
}
