import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';

@Component({
    selector: 'app-product',
    templateUrl: './product.component.html',
    styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

    productService = inject(ProductService)
    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)

    product$!: Observable<Product>
    product!: Product
    productID = ''


    ngOnInit(): void {
        this.productID = this.activatedRoute.snapshot.params['productID']
        this.product$ = this.productService.getProduct(Number.parseInt(this.productID)).pipe(
            map(p => {
                this.product = p
                console.info(p)
                return p
            })
        )
    }

    editProduct() {
        this.productService.product = this.product
        this.router.navigate(['/editproduct/' + this.productID])
    }

    deleteProduct() {
        var result = confirm("Want to delete?");
        if (result) {
            this.productService.deleteProduct(this.productID).subscribe()
            this.router.navigate(['/addproduct/'])
        }
    }
}
