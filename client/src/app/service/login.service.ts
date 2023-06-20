import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

const URL = 'http://localhost:8080'

@Injectable({
  providedIn: 'root'
})
export class LoginService {

    http = inject(HttpClient)


    login(data: any): Observable<any> {

        return this.http.post<any>('/user/login', data)
    }

    signup(data: any): Observable<any> {

        return this.http.post<any>('/user/signup', data)
    }

    logout() {
        return this.http.delete('/user/logout')
    }

}
