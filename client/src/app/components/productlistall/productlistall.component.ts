import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { MatPaginator, MatPaginatorDefaultOptions, PageEvent } from '@angular/material/paginator';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable, Subscription, map } from 'rxjs';
import { Product } from 'src/app/models/product.model';
import { LoginService } from 'src/app/service/login.service';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
    selector: 'app-productlistall',
    templateUrl: './productlistall.component.html',
    styleUrls: ['./productlistall.component.css']
})
export class ProductlistallComponent implements OnInit, OnDestroy, AfterViewInit {

    router = inject(Router)
    productService = inject(ProductService)
    loginService = inject(LoginService)
    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)

    email = ''
    productlist!: Product[]
    modifiedProductlist!: Product[]
    productID!: number
    @ViewChild('paginator') paginator!: MatPaginator;
    pageSize = 0
    length!: number
    pageIndex = 0
    loggedIn = this.storageService.isLoggedIn()
    tags!: string[]
    prods!: Subscription

    ngOnInit(): void {
        this.email = this.storageService.getUser().email
        this.pageSize = this.activatedRoute.snapshot.queryParams['pageSize']
        this.pageIndex = this.activatedRoute.snapshot.queryParams['pageIndex']
        this.productService.getAllOtherProductsCount(this.email).subscribe(data => {
            this.length = data
        });
        this.prods = this.productService.getAllOtherProducts(this.email, this.pageSize, this.pageIndex).pipe(
            map(data => {
                console.info(data)
                this.productlist = data
                return this.productlist
            })
        ).subscribe()

        this.productService.getAllProductTags().subscribe(data => {
            console.info('data', data)
            this.tags = this.shuffleArray(data).slice(0, this.randomIntFromInterval(10, 15))
        })
    }

    ngAfterViewInit(): void {
        this.paginator.pageIndex = this.pageIndex
    }

    ngOnDestroy(): void {
        this.prods.unsubscribe()
    }

    gotoProduct(product: Product) {
        this.productID = product.productID
        this.router.navigate(['/product/' + this.productID])
    }

    handlePageEvent(e: PageEvent) {
        this.pageSize = e.pageSize;
        this.pageIndex = e.pageIndex;
        this.productService.getAllOtherProducts(this.email, this.pageSize, this.pageIndex).pipe(
            map(data => {
                this.productlist = data
                return this.productlist
            })
        ).subscribe(() => {
            this.router.navigate([], {
                relativeTo: this.activatedRoute,
                queryParams: {
                    pageSize: e.pageSize,
                    pageIndex: e.pageIndex
                }
            })
        })
    }

    selectTag(event: any) {
        const tagContent = event.srcElement.textContent
        console.info(tagContent)
    }

    back() {
        this.router.navigate(['..'])
    }

    // private functions
    randomIntFromInterval(min: number, max: number) {
        return Math.floor(Math.random() * (max - min + 1) + min)
    }

    shuffleArray(array: string[]) {
        let currentIndex = array.length, randomIndex;
        while (currentIndex != 0) {
            randomIndex = Math.floor(Math.random() * currentIndex);
            currentIndex--;
            [array[currentIndex], array[randomIndex]] = [
                array[randomIndex], array[currentIndex]];
        }
        return array;
    }

}
