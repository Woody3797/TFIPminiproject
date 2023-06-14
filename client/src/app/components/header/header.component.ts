import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

    router = inject(Router)
    productService = inject(ProductService)

    username = 'admin'
    loggedIn = false;


    goToProfile() {
        this.router.navigate(['/profile'])
    }

    addProduct() {
        this.router.navigate(['/addproduct'])
    }

    listProducts() {
        this.router.navigate([ this.username + '/productlist'])
    }
}
