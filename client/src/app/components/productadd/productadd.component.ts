import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
    uploaded = false

    ngOnInit(): void {
        this.createAddProductForm()

    }

    createAddProductForm() {
        this.form = this.fb.group({
            productName: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
            price: this.fb.control<number>(Number.parseFloat(''), [Validators.required, Validators.min(0)]),
            description: this.fb.control<string>(''),
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
                this.router.navigate(['/product/' + data.productID])
            },
            error: e => {
                alert(e.message)
                // location.reload()
            }
        })
        this.uploaded = true
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
    
    limitPriceDecimal(event: any) {
        let dec = event.target.value.indexOf(".")
        let tooLong = event.target.value.length > dec + 3
        let invalidNum = isNaN(parseFloat(event.target.value))
        if ((dec >= 0 && tooLong) || invalidNum) {
            event.target.value = event.target.value.slice(0, -1)
        }
    }
    
    priceToDecimal(event: Event) {
        const target = event.target as HTMLInputElement
        target.value = parseFloat(target.value).toFixed(2);
        console.info(target.value)
    }

}
