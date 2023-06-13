import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/service/product.service';

@Component({
  selector: 'app-productlist',
  templateUrl: './productlist.component.html',
  styleUrls: ['./productlist.component.css']
})
export class ProductlistComponent implements OnInit {

    router = inject(Router)
    productService = inject(ProductService)
    activatedRoute = inject(ActivatedRoute)

    username = 'admin'
    productlist$!: Observable<Product[]>
    productlist!: Product[]

    ngOnInit(): void {
        // this.username = this.activatedRoute.snapshot.params['username']
        this.productService.getAllProducts(this.username).pipe(
            map(data => {
                this.productlist = data
                return this.productlist
            })
        ).subscribe()
    }
}
