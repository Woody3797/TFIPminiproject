import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductTags } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
    selector: 'app-producttags',
    templateUrl: './producttags.component.html',
    styleUrls: ['./producttags.component.css']
})
export class ProducttagsComponent implements OnInit {

    productService = inject(ProductService)
    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)
    router = inject(Router)

    productID!: number
    productTags!: ProductTags


    ngOnInit(): void {
        this.productID = +this.activatedRoute.snapshot.params['productID']
        this.productService.getProductTags(this.productID).subscribe(data => {
            this.productTags = data
        })
    }

    click(event: any) {
        const tagContent = event.srcElement.textContent
        this.router.navigate([this.storageService.getUser().email + '/allproducts'], {
            queryParams: {
                tag: tagContent
            }
        })
    }
}
