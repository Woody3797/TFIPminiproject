import { AfterViewInit, Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Image, Product, UploadProduct } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

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
    storageService = inject(StorageService)

    @ViewChild('productImage')
    productImage!: ElementRef

    productID = ''
    product: Product = {
        productID: 0,
        productName: '',
        description: '',
        price: 0,
        email: '',
        uploadTime: '',
        images: [],
        productStatus: ''
    }
    product$!: Observable<Product>
    form!: FormGroup
    upproduct!: UploadProduct
    images: File[] = []
    @ViewChild('price')
    price!: ElementRef


    ngOnInit(): void {
        this.productID = this.activatedRoute.snapshot.params['productID']
        this.createEditProductForm()
        this.productService.getProduct(Number.parseInt(this.productID)).subscribe(p => {
            this.product = this.createImages(p)
            this.form.patchValue({
                productName: this.product.productName,
                price: this.product.price,
                description: this.product.description,
            })
        })
    }

    createEditProductForm() {
        this.form = this.fb.group({
            productName: this.fb.control<string>('', [Validators.required, Validators.minLength(5)]),
            price: this.fb.control<number>(0, [Validators.required, Validators.min(0)]),
            description: this.fb.control<string>(''),
            productImage: this.fb.control<File | null>(null, [])
        })
    }

    editProductDetails() {
        this.upproduct = {
            productName: this.form.value.productName,
            price: Number(this.form.value.price.toFixed(2)),
            description: this.form.value.description,
            email: this.storageService.getUser().email
        }
        console.info(this.upproduct)
        this.productService.editProduct(this.upproduct, this.images, Number.parseInt(this.productID)).subscribe({
            next: data => {
                console.info(data)
                this.router.navigate(['/product/' + this.productID])
            },
            error: e => {
                console.info(e.message)
                // location.reload()
            }
        })
    }

    showPreview(event: any) {
        const imageFile: Image = {
            imageID: 0,
            imageName: event.target.files[0].name,
            type: event.target.files[0].type,
            imageBytes: event.target.files[0],
            url: URL.createObjectURL(event.target.files[0]),
        }
        this.product.images.push(imageFile)
        const file: File = this.productImage.nativeElement.files[0]
        this.images.push(file)
        this.limitImages()
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
        return this.form.invalid || this.images.length == 0
    }

    limitPriceDecimal(event: Event) {
        const target = event.target as HTMLInputElement
        const separator = '.'
        const regex = new RegExp(`^\\d*(${separator}\\d{0,2})?$`)
        if (!regex.test(target.value)) {
            target.value = target.value.slice(0, -1)
        }
    }

    priceToDecimal(event: Event) {
        const target = event.target as HTMLInputElement
        target.value = parseFloat(target.value).toFixed(2);
    }

    dataURItoBlob(data: string, type: string) {
        const byteString = window.atob(data);
        const arrayBuffer = new ArrayBuffer(byteString.length);
        const int8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < byteString.length; i++) {
            int8Array[i] = byteString.charCodeAt(i);
        }
        const blob = new Blob([int8Array], { type: type });
        return blob;
    }

    createImages(product: Product): Product {
        const productImages: Image[] = product.images
        const images: Image[] = []
        for (var i of productImages) {
            const imageBlob = this.dataURItoBlob(i.imageBytes, i.type)
            const imageFile = new File([imageBlob], i.imageName, { type: i.type })
            this.images.push(imageFile)
            const image: Image = {
                imageID: i.imageID,
                imageName: i.imageName,
                type: i.type,
                imageBytes: imageFile,
                url: window.URL.createObjectURL(imageFile)
            }
            images.push(image)
        }
        product.images = images
        return product
    }

}
