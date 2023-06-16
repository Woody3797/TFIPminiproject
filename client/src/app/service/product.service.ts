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
    product!: Product

    addNewProduct(upproduct: UploadProduct, productImages: File[]): Observable<Product> {
        const fdata = new FormData()
        fdata.set('product', new Blob([JSON.stringify(upproduct)], {type: 'application/json'}))
        for (const image of productImages) {
            fdata.append('productImages', image)
        }
        
        return this.http.post<Product>('/api/addnewproduct', fdata) 
    }

    getProduct(productID: number) {
        return this.http.get<Product>('/api/product/' + productID)
    }

    getAllProducts(username: string, pageSize: number, pageIndex: number) {
        const params = new HttpParams().append('pageSize', pageSize).append('pageIndex', pageIndex)

        return this.http.get<Product[]>('/api/' + username + '/productlist', {params})
    }

    editProduct(upproduct: UploadProduct, productImages: File[], productID: number): Observable<Product> {
        const fdata = new FormData()
        fdata.set('product', new Blob([JSON.stringify(upproduct)], {type: 'application/json'}))
        for (const image of productImages) {
            fdata.append('productImages', image)
        }
        
        return this.http.put<Product>('/api/editproduct/' + productID, fdata)
    }

    deleteProduct(productID: string): Observable<string> {
        return this.http.delete<string>('/api/deleteproduct/' + productID)
    }
}
