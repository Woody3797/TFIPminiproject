import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Product, UploadProduct } from '../models/product.model';

const URL = 'http://localhost:8080'

@Injectable({
  providedIn: 'root'
})
export class ProductService {

    http = inject(HttpClient)

    product$!: Observable<Product>

    addNewProduct(upproduct: UploadProduct, productImages: File[]): Observable<Product> {
        const fdata = new FormData()
        fdata.set('product', new Blob([JSON.stringify(upproduct)], {type: 'application/json'}))
        for (const image of productImages) {
            fdata.append('productImages', image)
        }
        
        this.product$ = this.http.post<Product>(URL + '/addnewproduct', fdata)
        return this.product$
    }

    getProduct(productID: number) {
        const params = new HttpParams().set('productID', productID)

        return this.http.get<Product>(URL + '/product/' + productID)
    }
}
