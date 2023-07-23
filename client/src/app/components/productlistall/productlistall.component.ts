import { Location } from '@angular/common';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { ActivatedRoute, Event, NavigationStart, Router } from '@angular/router';
import { Subscription, map } from 'rxjs';
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
    location = inject(Location)
    productService = inject(ProductService)
    loginService = inject(LoginService)
    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)

    productlist!: Product[]
    modifiedProductlist!: Product[]
    productID!: number
    @ViewChild('paginator') paginator!: MatPaginator;
    email = this.storageService.getUser().email
    pageSize = this.activatedRoute.snapshot.queryParams['pageSize']
    pageIndex = this.activatedRoute.snapshot.queryParams['pageIndex']
    tag = this.activatedRoute.snapshot.queryParams['tag']
    length!: number
    loggedIn = this.storageService.isLoggedIn()
    tags!: string[]
    prods!: Subscription

    ngOnInit(): void {
        if (this.tag != undefined) {
            this.productService.getProductsByTag(this.email, this.tag).subscribe(data => {
                this.productlist = data
                this.length = data.length
            })
        }

        this.productService.getAllOtherProductsCount(this.email).subscribe(data => {
            this.length = data
        });
        this.prods = this.productService.getAllOtherProducts(this.email, this.pageSize, this.pageIndex).pipe(
            map(data => {
                this.productlist = data
                return this.productlist
            })
        ).subscribe()
        this.productService.getAllProductTags().subscribe(data => {
            this.tags = this.shuffleArray(data).slice(0, this.randomIntFromInterval(13, 17))
        })

        this.router.events.subscribe((event: Event) => {
            if (event instanceof NavigationStart && event.navigationTrigger == 'popstate') {
                window.location.reload();
            }
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
        this.productService.getProductsByTag(this.email, tagContent).subscribe(data => {
            this.productlist = data
            this.length = data.length
            this.router.navigate([], {
                relativeTo: this.activatedRoute,
                queryParams: {
                    tag: tagContent
                }
            })
        })
    }

    back() {
        this.router.navigate(['/'])
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
