import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Image, Product, UploadProduct } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';

@Component({
    selector: 'app-productedit',
    templateUrl: './productedit.component.html',
    styleUrls: ['./productedit.component.css']
})
export class ProducteditComponent implements OnInit {

    fb = inject(FormBuilder)
    productService = inject(ProductService)
    activatedRoute = inject(ActivatedRoute)
    router = inject(Router)

    @ViewChild('productImage')
    productImage!: ElementRef

    productID = ''
    product!: Product
    product$!: Observable<Product>
    form!: FormGroup
    upproduct!: UploadProduct
    images: File[] = []


    ngOnInit(): void {
        this.createEditProductForm()
        this.productID = this.activatedRoute.snapshot.params['productID']
        this.productService.getProduct(Number.parseInt(this.productID)).subscribe(p => {
            this.product = p
            console.info(p.images)
            this.form.patchValue({
                productName: this.product.productName,
                price: this.product.price,
                description: this.product.description
            })
        })
    }

    createEditProductForm() {
        this.form = this.fb.group({
            productName: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
            price: this.fb.control<number>(0, [Validators.required, Validators.min(0)]),
            description: this.fb.control<string>(''),
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
        
        const file: File = this.productImage.nativeElement.files[0]
        this.images.push(file)
        this.limitImages()
    }

    remove(i: number) {
        this.product.images.splice(i, 1)
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

    decimalFilter(event: any) {
        const reg = /^-?\d*(\.\d{0,2})?$/;
        let input = event.target.value + String.fromCharCode(event.charCode);

        if (!reg.test(input)) {
            event.preventDefault();
        }
    }

    convertBase64ToFile(data: string) {
        let dataStr = atob(data);
        let n = dataStr.length;
        let dataArr = new Uint8Array(n);
        while (n--) {
            dataArr[n] = dataStr.charCodeAt(n);
        }
        let file = new File([dataArr], 'File.png', { type: this.product.images[0].type });

        return file;
    }
}
