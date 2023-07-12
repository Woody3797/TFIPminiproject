import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ProfilePic } from '../models/profile.model';

@Injectable({
    providedIn: 'root'
})
export class ProfileService {

    http = inject(HttpClient)
    
    editProfile(email: string, password: string, profileImage: File): Observable<ProfilePic> {
        const fdata = new FormData
        fdata.set('email', email)
        fdata.set('password', password)
        fdata.set('profileImage', profileImage)

        return this.http.post<ProfilePic>('/profile/editprofile', fdata)
    }

    getProfilePic(email: string): Observable<ProfilePic> {

        return this.http.get<ProfilePic>('/profile/getprofilepic/' + email)
    }

    deleteProfilePic(email: string) {

        return this.http.delete('/profile/deleteprofilepic/' + email)
    }
}
