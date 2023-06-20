import { Injectable } from '@angular/core';

const USERKEY = 'authenticateduser'

@Injectable({
  providedIn: 'root'
})
export class StorageService {

    clearStorage() {
        window.sessionStorage.clear()
    }

    saveUser(userData: any) {
        window.sessionStorage.removeItem(USERKEY)
        window.sessionStorage.setItem(USERKEY, JSON.stringify(userData))
    }

    getUser(): any {
        const user = window.sessionStorage.getItem(USERKEY)
        if (user) {
            return JSON.parse(user)
        } else {
            return {}
        }
    }

    isLoggedIn(): boolean {
        const user = window.sessionStorage.getItem(USERKEY)
        if (user) {
            return true
        } else {
            return false
        }
    }
}
