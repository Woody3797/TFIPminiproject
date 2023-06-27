import { Location } from '@angular/common';
import { Component, Inject, OnInit, inject } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
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
    dialog = inject(MatDialog)

    product$!: Observable<Product>
    product!: Product
    productID = ''
    loggedIn = this.storageService.isLoggedIn()
    isUser = false
    productStatus = ''
    imgData!: any

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
        Swal.fire({
            title: 'Delete Product',
            text: 'Are you sure you want to delete product?',
            showDenyButton: true,
            confirmButtonText: 'Yes'
        }).then(result => {
            if (result.value) {
                this.productService.deleteProduct(this.productID).subscribe(d => {
                    this.router.navigate([this.product.email + '/productlist/'])
                })
            }
        })
    }

    buyProduct() {
        Swal.fire({
            title: 'Place Offer',
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
            title: 'Cancel Offer',
            text: 'Do you wish to cancel your offer?',
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

    acceptOffer() {
        Swal.fire({
            title: 'Accept Offer',
            text: 'Are you sure you want to accept the offer?',
            showDenyButton: true,
            confirmButtonText: 'Yes'
        }).then(result => {
            console.info(result)
        })
    }

    enlargeImg(event: any) {
        var img = event.target
        const dialogRef = this.dialog.open(ImageDialog, {
            maxHeight: '70%',
            data: {
                imgData: img,
                product: this.product
            }
        })
    }
    
    goBackToProductlist() {
        this.location.back()
    }
}


@Component({
    selector: 'image-dialog',
    templateUrl: 'image-dialog.html',
    standalone: true,
    imports: [MatDialogModule],
})
export class ImageDialog implements OnInit {

    constructor(@Inject(MAT_DIALOG_DATA) public data: ProductComponent) {}

    img!: string
    imageID = +this.data.imgData.id + 1
    size = this.data.product.images.length
    
    ngOnInit(): void {
        this.img = this.data.imgData.src
        // this.productService.getProduct(this.productID).subscribe(d => {
        //     this.product = d
        //     this.img = this.product.images[this.data.imageID]
        //     console.info(this.img)
        // })
    }
}
