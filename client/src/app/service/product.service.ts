import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';

const URL = 'http://localhost:8080'

@Injectable({
  providedIn: 'root'
})
export class ProductService {

    http = inject(HttpClient)


    addNewProduct(product: Product, productImage: File): Observable<Product> {
        const fdata = new FormData()
        fdata.set('data', JSON.stringify(product))
        fdata.set('productImage', productImage)
        return this.http.post<Product>(URL + '/addnewproduct', fdata)
    }
}
