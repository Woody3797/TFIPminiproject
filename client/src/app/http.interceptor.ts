import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable, catchError, throwError } from "rxjs";
import { StorageService } from "./service/storage.service";

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {

    storageService = inject(StorageService)

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const jwtToken = this.storageService.getToken()

        if (jwtToken) {
            req = req.clone({
                setHeaders: {
                    Authorization: jwtToken
                },
                withCredentials: true,
            })
        }

        return next.handle(req).pipe(
            catchError(error => {
                console.info(error)
                return throwError(() => error)
            })
        )
    }
}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
];