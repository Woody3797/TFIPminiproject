import { Component, inject } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-productlistall',
  templateUrl: './productlistall.component.html',
  styleUrls: ['./productlistall.component.css']
})
export class ProductlistallComponent {

    router = inject(Router)
    productService = inject(ProductService)
    loginService = inject(LoginService)
    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)

    email = ''
    productlist$!: Observable<Product[]>
    productlist!: Product[]
    modifiedProductlist!: Product[]
    productID!: number

    pageSize = 5
    length!: number
    pageIndex = 0
    loggedIn = this.storageService.isLoggedIn()

    ngOnInit(): void {
        this.email = this.storageService.getUser().email
        this.pageSize = this.activatedRoute.snapshot.queryParams['pageSize']
        this.pageIndex = this.activatedRoute.snapshot.queryParams['pageIndex']
        this.productService.getAllOtherProducts(this.email, 99, this.pageIndex).pipe(
            map(data => {
                this.productlist = data
                this.productlist = this.productlist.slice(this.pageSize*this.pageIndex, this.pageSize*(this.pageIndex + 1))
                this.length = data.length
                return this.productlist
            })
        ).subscribe()
    }

    gotoProduct(product: Product) {
        this.productID = product.productID
        this.router.navigate(['/product/' + this.productID])
    }

    handlePageEvent(e: PageEvent) {
        this.pageSize = e.pageSize;
        this.pageIndex = e.pageIndex;
        this.productService.getAllOtherProducts(this.email, this.pageSize, this.pageIndex).pipe(
            map(data => {
                this.productlist = data
                // this.length = data.length
                return this.productlist
            })
        ).subscribe(() => {
            this.router.navigate([], {
                relativeTo: this.activatedRoute,
                queryParams: {
                    pageSize: e.pageSize,
                    pageIndex: e.pageIndex
                }
            })
        })
    }

}
