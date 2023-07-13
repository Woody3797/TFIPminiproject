import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { LikedProducts, OrderDetails, Product, ProductTags, UploadProduct } from '../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

    http = inject(HttpClient)

    product$!: Observable<Product>
    product!: Product
    productID!: number

    addNewProduct(upproduct: UploadProduct, productImages: File[]): Observable<Product> {
        const fdata = new FormData()
        fdata.set('product', new Blob([JSON.stringify(upproduct)], {type: 'application/json'}))
        for (const image of productImages) {
            fdata.append('productImages', image)
        }
        
        return this.http.post<Product>('/api/addnewproduct', fdata) 
    }

    getProduct(productID: number): Observable<Product> {
        this.productID = productID
        console.info(this.productID)
        return this.http.get<Product>('/api/product/' + productID)
    }

    getAllProducts(email: string, pageSize: number, pageIndex: number): Observable<Product[]> {
        const params = new HttpParams().append('pageSize', pageSize).append('pageIndex', pageIndex)

        return this.http.get<Product[]>('/api/' + email + '/productlist', {params})
    }

    getAllOtherProducts(email: string, pageSize: number, pageIndex: number): Observable<Product[]> {
        const params = new HttpParams().append('pageSize', pageSize).append('pageIndex', pageIndex)

        return this.http.get<Product[]>('/api/' + email + '/allproducts', {params})
    }

    editProduct(upproduct: UploadProduct, productImages: File[], productID: number): Observable<Product> {
        const fdata = new FormData()
        fdata.set('product', new Blob([JSON.stringify(upproduct)], {type: 'application/json'}))
        for (const image of productImages) {
            fdata.append('productImages', image)
        }
        
        return this.http.put<Product>('/api/editproduct/' + productID, fdata)
    }

    deleteProduct(productID: number): Observable<string> {
        return this.http.delete<string>('/api/deleteproduct/' + productID)
    }

    buyProduct(productID: string, buyer: string, seller: string): Observable<OrderDetails> {
        const data = new FormData()
        data.set('productID', productID)
        data.set('buyer', buyer)
        data.set('seller', seller)
        data.set('status', 'pending sale')
        
        return this.http.post<OrderDetails>('/api/buyproduct', data)
    }

    cancelBuyProduct(productID: string, buyer: string, seller: string): Observable<OrderDetails> {
        const data = new FormData()
        data.set('productID', productID)
        data.set('buyer', buyer)
        data.set('seller', seller)
        
        return this.http.post<OrderDetails>('/api/cancelpending', data)
    }

    getOrderDetails(productID: number): Observable<OrderDetails> {
        return this.http.get<OrderDetails>('/api/getorderdetails/' + productID)
    }

    acceptOrder(productID: string, buyer: string, seller: string): Observable<any> {
        const data = new FormData
        data.set('productID', productID)
        data.set('buyer', buyer)
        data.set('seller', seller)

        return this.http.post<any>('/api/acceptorder', data)
    }
    
    getProductTags(productID: number): Observable<ProductTags> {
        return this.http.get<ProductTags>('/api/getproducttags/' + productID)
    }

    getAllProductTags(): Observable<string[]> {
        return this.http.get<string[]>('/api/getallproducttags')
    }

    getAllOtherProductsCount(email: string): Observable<number> {
        return this.http.get<number>('/api/' + email + '/allproductscount')
    }

    getProductsByTag(email: string, tag: string): Observable<Product[]> {
        return this.http.get<Product[]>('/api/' + email + '/allproducts/' + tag)
    }

    likeProduct(email: string, productID: number, like: boolean): Observable<any> {
        const params = new HttpParams().append('email', email)
        .append('productID', productID)
        .append('like', like)

        return this.http.post<any>('/api/likeproduct', params)
    }

    getLikedProductIDs(email: string): Observable<LikedProducts> {
        return this.http.get<LikedProducts>('/api/getlikedproductIDs/' + email)
    }

    getWatchlist(email: string): Observable<Product[]> {
        return this.http.get<Product[]>('/api/watchlist/' + email)
    }

}
