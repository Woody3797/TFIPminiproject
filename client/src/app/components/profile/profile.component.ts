import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, lastValueFrom, map } from 'rxjs';
import { ProfileService } from 'src/app/service/profile.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

    storageService = inject(StorageService)
    activatedRoute = inject(ActivatedRoute)
    profileService = inject(ProfileService)
    router = inject(Router)
    fb = inject(FormBuilder)

    email = ''
    form!: FormGroup
    isLoggedIn = this.storageService.isLoggedIn()
    profileImage!: string 
    image$!: Subscription
    deletePic = false

    ngOnInit(): void {
        this.email = this.storageService.getUser().email
        this.createForm()
        this.image$ = this.profileService.getProfilePic(this.email).subscribe({
            next: data => {
                this.profileImage = ''
                setTimeout(() => {
                    console.info('sleeping')
                    this.profileImage = data.url
                }, 1111)
                this.form.markAsPristine()
            }
        })
    }

    ngOnDestroy(): void {
        this.image$.unsubscribe()
    }

    createForm() {
        this.form = this.fb.group({
            password: this.fb.control('', [Validators.minLength(3)]),
            profileImage: this.fb.control<File | null>(null, [])
        })
    }

    showPreview(event: any) {
        this.profileImage = URL.createObjectURL(event.target.files[0])
        this.form.patchValue({
            profileImage: event.target.files[0]
        })
        console.info(this.form.value.profileImage)
        this.form.markAsDirty()
    }

    editProfile() {
        console.info(this.form.value)
        this.profileService.editProfile(this.email, this.form.value.password, this.form.value.profileImage).pipe(
            map(data => {
                this.profileImage = data.url
                setTimeout(() => {location.reload()}, 500)
            })
        ).subscribe()

        if (this.deletePic) {
            lastValueFrom(this.profileService.deleteProfilePic(this.email)).then(data => {
                console.info(data)
            })
        }
    }

    removePic() {
        this.deletePic = true
        this.profileImage = ''
        this.form.patchValue({
            profileImage: null
        })
        this.form.markAsDirty()
    }

    invalid() {
        return this.form.invalid || this.form.pristine
    }

}
