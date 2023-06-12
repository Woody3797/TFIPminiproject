import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Image, Product, UploadProduct } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-productadd',
  templateUrl: './productadd.component.html',
  styleUrls: ['./productadd.component.css']
})
export class ProductaddComponent implements OnInit {

    fb = inject(FormBuilder)
    router = inject(Router)
    productService = inject(ProductService)

    @ViewChild('productImage')
    productImage!: ElementRef

    form!: FormGroup
    product: Product = {
        productID: 0,
        username: 'admin',
        productName: '',
        description: '',
        price: 0,
        uploadTime: '',
        images: []
    }
    upproduct: UploadProduct = {
        productName: '',
        description: '',
        price: 0
    }
    images: File[] = []

    ngOnInit(): void {
        this.createAddProductForm()
    }

    createAddProductForm() {
        this.form = this.fb.group({
            productName: this.fb.control<string>(Math.random().toString(36).slice(-5).replace(/\d/g, String.fromCharCode(Math.random()*26+97)), [Validators.required, Validators.minLength(5)]),
            price: this.fb.control<number>(Math.floor(Math.random()*20)+1.00, [Validators.required, Validators.min(0)]),
            description: this.fb.control<string>('test'),
            productImage: this.fb.control<File | null>(null, [Validators.required, Validators.max(4)])
        })
    }

    uploadProductDetails() {
        this.upproduct.productName = this.form.value.productName
        this.upproduct.price = this.form.value.price
        this.upproduct.description = this.form.value.description
        console.info(this.upproduct)
        this.productService.addNewProduct(this.upproduct, this.images).subscribe({
            next: data => {
                this.router.navigate(['/product', data.productID])
            },
            error: e => {
                alert(e.message)
                // location.reload()
            }
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
        this.form.patchValue({productImage: null})
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

    decimalFilter(event: any) {
        const reg = /^-?\d*(\.\d{0,2})?$/;
        let input = event.target.value + String.fromCharCode(event.charCode);
     
        if (!reg.test(input)) {
            event.preventDefault();
        }
     }
}
