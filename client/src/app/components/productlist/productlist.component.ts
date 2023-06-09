import { Component, OnInit, inject } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-productlist',
  templateUrl: './productlist.component.html',
  styleUrls: ['./productlist.component.css']
})
export class ProductlistComponent implements OnInit {

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

    pageSize = 10
    length!: number
    pageIndex = 0

    ngOnInit(): void {
        this.email = this.storageService.getUser().email
        this.productService.getAllProducts(this.email, 99, this.pageIndex).pipe(
            map(data => {
                this.productlist = data
                this.modifiedProductlist = this.productlist.slice(this.pageSize*this.pageIndex, this.pageSize*(this.pageIndex + 1))
                this.length = data.length
                console.info(this.productlist)
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
        this.modifiedProductlist = this.productlist.slice(e.pageSize*e.pageIndex, e.pageSize*(e.pageIndex + 1))
    }
}
