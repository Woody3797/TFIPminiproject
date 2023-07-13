import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit {

    router = inject(Router)
    productService = inject(ProductService)
    loginService = inject(LoginService)
    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)

    email = this.storageService.getUser().email
    productlist$!: Observable<Product[]>
    productlist!: Product[]
    productID!: number

    ngOnInit(): void {
        this.productlist$ = this.productService.getWatchlist(this.email)
    }

    gotoProduct(product: Product) {
        this.productID = product.productID
        this.router.navigate(['/product/' + this.productID])
    }

}
