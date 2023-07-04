import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { map } from 'rxjs';
import { Image, Product, UploadProduct } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
    selector: 'app-productadd',
    templateUrl: './productadd.component.html',
    styleUrls: ['./productadd.component.css']
})
export class ProductaddComponent implements OnInit {

    fb = inject(FormBuilder)
    router = inject(Router)
    productService = inject(ProductService)
    storageService = inject(StorageService)

    @ViewChild('productImage')
    productImage!: ElementRef

    form!: FormGroup
    product: Product = {
        productID: 0,
        email: '',
        productName: '',
        description: '',
        price: 0,
        uploadTime: '',
        images: [],
        productStatus: ''
    }
    upproduct: UploadProduct = {
        productName: '',
        description: '',
        price: 0,
        email: ''
    }
    images: File[] = []
    productID!: number

    ngOnInit(): void {
        this.createAddProductForm()

    }

    createAddProductForm() {
        this.form = this.fb.group({
            productName: this.fb.control<string>(Math.random().toString(36).slice(-5).replace(/\d/g, String.fromCharCode(Math.random() * 26 + 97)), [Validators.required, Validators.minLength(5)]),
            price: this.fb.control<number>(Math.floor(Math.random() * 20) + 1.00, [Validators.required, Validators.min(0)]),
            description: this.fb.control<string>('test'),
            productImage: this.fb.control<File | null>(null, [])
        })
    }

    uploadProductDetails() {
        this.upproduct.productName = this.form.value.productName
        this.upproduct.price = this.form.value.price
        this.upproduct.description = this.form.value.description
        this.upproduct.email = this.storageService.getUser().email
        this.productService.addNewProduct(this.upproduct, this.images).subscribe({
            next: data => {
                console.info(data)
                // this.router.navigate(['/product/' + data.productID])
            },
            error: e => {
                alert(e.message)
                // location.reload()
            }
        })

        this.productService.uploadImageImagga(this.images).subscribe(data => {
            console.info(data)
        })
    }

    showPreview(event: any) {
        const imageFile: Image = {
            imageID: 0,
            imageName: '',
            type: '',
            imageBytes: event.target.files[0],
            url: URL.createObjectURL(event.target.files[0]),
        }
        this.product.images.push(imageFile)
        const file: File = this.productImage.nativeElement.files[0]
        this.images.push(file)
        this.limitImages()
    }

    resetForm() {
        this.product.images.length = 0
        this.createAddProductForm()
    }

    remove(i: number) {
        this.product.images.splice(i, 1)
        this.images.splice(i, 1)
        this.form.patchValue({ productImage: null })
        this.form.get('productImage')?.enable()
    }

    limitImages(): boolean {
        if (this.product.images.length > 3) {
            this.form.get('productImage')?.disable()
        }
        return this.product.images.length < 4
    }

    invalidForm() {
        return this.form.invalid || this.product.images.length == 0
    }

    limitPriceDecimal(event: Event) {
        const target = event.target as HTMLInputElement
        const separator = '.'
        const regex = new RegExp(`^\\d*(${separator}\\d{0,2})?$`)
        if (!regex.test(target.value)) {
            target.value = target.value.slice(0, -1)
        }
        target.value = parseFloat(target.value).toFixed(2);
    }

    priceToDecimal(event: Event) {
        const target = event.target as HTMLInputElement
        target.value = parseFloat(target.value).toFixed(2);
    }

}
