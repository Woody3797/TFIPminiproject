import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-forgotpassword',
  templateUrl: './forgotpassword.component.html',
  styleUrls: ['./forgotpassword.component.css']
})
export class ForgotpasswordComponent implements OnInit {

    fb = inject(FormBuilder)
    storageService = inject(StorageService)
    loginService = inject(LoginService)
    router = inject(Router)

    form!: FormGroup
    isLoggedIn = this.storageService.isLoggedIn()


    ngOnInit(): void {
        this.createForm()

    }

    createForm() {
        this.form = this.fb.group({
            email: this.fb.control('a@a.com', [Validators.required, Validators.email])
        })
    }

    resetPassword() {
        this.loginService.resetPassword(this.form.value.email).subscribe(data => {
            console.info(data)
        })
        // alert('Email with new password has been sent!')
        // this.router.navigate(['/login'])
    }

    invalid() {
        return this.form.invalid || this.isLoggedIn == true
    }
}
