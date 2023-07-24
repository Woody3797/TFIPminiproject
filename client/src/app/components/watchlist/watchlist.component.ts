import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, Subscription, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-watchlist',
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css']
})
export class WatchlistComponent implements OnInit, OnDestroy {

    router = inject(Router)
    productService = inject(ProductService)
    loginService = inject(LoginService)
    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)

    email = this.storageService.getUser().email
    productlist$!: Subscription
    productlist!: Product[]
    productID!: number
    isLoaded = false

    ngOnInit(): void {
        this.productlist$ = this.productService.getWatchlist(this.email).subscribe(data => {
            this.productlist = data
            this.isLoaded = true
        })
    }

    ngOnDestroy(): void {
        this.productlist$.unsubscribe()
    }

    gotoProduct(product: Product) {
        this.productID = product.productID
        this.router.navigate(['/product/' + this.productID])
    }

}
