import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { throwError } from 'rxjs';
import { LoginRequest, SignupRequest } from 'src/app/models/login.model';
import { LoginService } from 'src/app/service/login.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

    fb = inject(FormBuilder)
    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)
    loginService = inject(LoginService)
    storageService = inject(StorageService)

    signupForm!: FormGroup
    loginForm!: FormGroup
    isLoggedIn = false


    ngOnInit(): void {
        this.signupForm = this.createSignupForm()
        this.loginForm = this.createLoginForm()

        if (this.storageService.isLoggedIn()) {
            this.isLoggedIn = true
        }
    }

    createSignupForm(): FormGroup {
        return this.signupForm = this.fb.group({
            username: this.fb.control('', [Validators.required, Validators.minLength(4)]),
            email: this.fb.control('', [Validators.required, Validators.email]),
            password: this.fb.control('', [Validators.required, Validators.minLength(3)])
        })
    }

    createLoginForm(): FormGroup {
        return this.loginForm = this.fb.group({
            username: this.fb.control('admin', [Validators.required, Validators.minLength(4)]),
            password: this.fb.control('123', [Validators.required, Validators.minLength(3)])
        })
    }

    login() {
        const loginRequest: LoginRequest = {
            username: this.loginForm.value.username,
            password: this.loginForm.value.password,
        }
        this.loginService.login(loginRequest).subscribe({
            next: res => {
                console.info(res)
                this.storageService.saveUser(res)
                if (this.loginForm.value.username == res.username) {
                    this.router.navigate([res.username + '/productlist'])
                }
            },
            error: err => {
                console.info(err)
                alert(err.error.error)
            }
        })
    }

    signup() {
        const signupRequest: SignupRequest = {
            username: this.signupForm.value.username,
            email: this.signupForm.value.email,
            password: this.signupForm.value.password
        }
        this.loginService.signup(signupRequest).subscribe({
            next: res => {
                console.info(res)
                this.storageService.saveUser(res)
                if (this.signupForm.value.username == res.username) {
                    this.router.navigate([res.username + '/profile'])
                } else {
                    throwError(() => new Error('Unable to login user: ' + res.username))
                }
            },
            error: err => {
                console.info(err)
                alert(err.error.error)
            }
        })
    }
}
