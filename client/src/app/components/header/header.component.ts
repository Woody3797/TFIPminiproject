import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

    router = inject(Router)
    productService = inject(ProductService)
    loginService = inject(LoginService)

    username = ''
    loggedIn = false;


    ngOnInit(): void {
        this.username = this.loginService.username
    }

    gotoProfile() {
        this.router.navigate(['/profile'])
    }

    addProduct() {
        this.router.navigate(['/addproduct'])
    }

    listProducts() {
        this.router.navigate([ this.username + '/productlist'])
    }
}
