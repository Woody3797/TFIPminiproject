import { inject } from "@angular/core"
import { StorageService } from "./service/storage.service"
import { CanActivateFn, Router } from "@angular/router"

export const authGuard: CanActivateFn = () => {
    const storageService = inject(StorageService)
    const router = inject(Router)

    if (storageService.isLoggedIn()) {
        return true
    } else {
        router.navigate(['/login'])
        return false
    }

}