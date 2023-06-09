import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Product } from 'src/app/models/product.model';
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
    imageFile!: string | null

    ngOnInit(): void {
        this.createAddProductForm()
    }

    createAddProductForm() {
        this.form = this.fb.group({
            productName: this.fb.control<string>('a', [Validators.required]),
            price: this.fb.control<number>(2, [Validators.required, Validators.min(0)]),
            description: this.fb.control<string>(''),
            productImage: this.fb.control<File | null>(null, [Validators.required])
        })
    }

    uploadProductDetails() {
        const image: File = this.productImage.nativeElement.files[0]
        var product = <Product>{}
        product.productName = this.form.value.productName
        product.price = this.form.value.price
        product.description = this.form.value.description
        this.productService.addNewProduct(product, image).subscribe()
    }

    showPreview(event: any) {
        this.imageFile = URL.createObjectURL(event.target.files[0])
    }

    resetForm() {
        this.createAddProductForm()
    }
}
