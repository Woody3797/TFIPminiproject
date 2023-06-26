import { Location } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';
import Swal from 'sweetalert2';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

    productService = inject(ProductService)
    activatedRoute = inject(ActivatedRoute)
    storageService = inject(StorageService)
    router = inject(Router)
    location = inject(Location)

    product$!: Observable<Product>
    product!: Product
    productID = ''
    loggedIn = this.storageService.isLoggedIn()
    isUser = false
    productStatus = ''


    ngOnInit(): void {
        this.productID = this.activatedRoute.snapshot.params['productID']
        this.product$ = this.productService.getProduct(Number.parseInt(this.productID)).pipe(
            map(p => {
                this.product = p
                console.info(p)
                this.productStatus = p.productStatus
                if (this.storageService.getUser().email == p.email) {
                    this.isUser = true
                }
                return p
            })
        )
    }

    editProduct() {
        this.productService.product = this.product
        this.router.navigate(['/editproduct/' + this.productID])
    }

    deleteProduct() {
        var result = confirm('Are you sure you want to delete?');
        if (result) {
            this.productService.deleteProduct(this.productID).subscribe()
            this.router.navigate(['/addproduct/'])
        }
    }

    buyProduct() {
        Swal.fire({
            title: 'Place Order',
            text: 'Do you wish to buy this item?',
            showDenyButton: true,
            confirmButtonText: 'Yes'
        }).then(result => {
            if (result.value) {
                this.productService.buyProduct(this.productID).subscribe(data => {
                    this.productStatus = data.productStatus
                })
            }
        })
    }

    cancelBuyProduct() {
        Swal.fire({
            title: 'Cancel Order',
            text: 'Do you wish to cancel your order?',
            showDenyButton: true,
            confirmButtonText: 'Yes'
        }).then(result => {
            if (result.value) {
                this.productService.cancelBuyProduct(this.productID).subscribe(data => {
                    this.productStatus = data.productStatus
                })
            }
        })
    }

    goBackToProductlist() {
        this.location.back()
    }
}
