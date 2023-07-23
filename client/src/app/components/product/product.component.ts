import { Location } from '@angular/common';
import { Component, Inject, OnInit, inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, lastValueFrom, map } from 'rxjs';
import { OrderDetails, Product } from 'src/app/models/product.model';
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
    fb = inject(FormBuilder)
    dialog = inject(MatDialog)

    product$!: Observable<Product>
    product!: Product
    orderDetails!: OrderDetails;
    productID!: number
    loggedIn = this.storageService.isLoggedIn()
    email = this.storageService.getUser().email
    isSeller = false
    productStatus = ''
    isOrdering = false
    isSold = false
    isLiked = false
    imgData!: any
    form!: FormGroup

    ngOnInit(): void {
        this.productID = +this.activatedRoute.snapshot.params['productID']
        this.createForm()
        this.product$ = this.productService.getProduct(this.productID).pipe(
            map(p => {
                this.product = p
                this.productStatus = p.productStatus
                if (this.storageService.getUser().email == p.email) {
                    this.isSeller = true
                }
                return p
            })
        )

        this.productService.getOrderDetails(this.productID).subscribe(data => {
            this.orderDetails = data
            if (this.orderDetails.buyers.includes(this.storageService.getUser().email)) {
                this.isOrdering = true
            }
            if (this.orderDetails.status.includes('sold')) {
                this.isSold = true
            }
        })

        lastValueFrom(this.productService.getLikedProductIDs(this.email)).then(data => {
            if (data.productIDs.includes(this.productID)) {
                this.isLiked = true
            }
        })
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
                this.productService.buyProduct(this.productID.toString(), this.storageService.getUser().email, this.product.email).subscribe(data => {
                    this.orderDetails = data
                    this.isOrdering = true
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
                this.productService.cancelBuyProduct(this.productID.toString(), this.storageService.getUser().email, this.product.email).subscribe(data => {
                    this.orderDetails = data
                    this.isOrdering = false
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
            if (result.value) {
                this.productService.acceptOrder(this.productID.toString(), this.form.value.buyer, this.product.email).subscribe(data => {
                    this.orderDetails = data
                    this.isSold = true
                })
            }
        })
    }

    chatWithSeller() {
        // console.info(this.productID)
        this.productService.productID = this.productID
        this.productService.seller = this.product.email
        this.router.navigate([ this.email + '/chat' ])
    }

    likeProduct() {
        this.isLiked = !this.isLiked
        lastValueFrom(this.productService.likeProduct(this.email, this.productID, this.isLiked)).then(data => {
            this.isLiked = data
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

    createForm() {
        this.form = this.fb.group({
            buyer: this.fb.control('', Validators.required)
        })
    }
    
    goBackInHistory() {
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
