import { SocialAuthService, SocialUser } from '@abacritt/angularx-social-login';
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
    authService = inject(SocialAuthService)

    signupForm!: FormGroup
    loginForm!: FormGroup
    isLoggedIn = false
    user!: SocialUser


    ngOnInit(): void {
        this.signupForm = this.createSignupForm()
        this.loginForm = this.createLoginForm()

        if (this.storageService.isLoggedIn()) {
            this.isLoggedIn = true
        }

        this.authService.authState.subscribe(user => {
            this.user = user;
            this.isLoggedIn = (user != null);
            this.loginService.googleLogin(user).subscribe({
                next: res => {
                    console.info(res)
                    this.storageService.saveUser(res)
                    this.router.navigate([res.email + '/productlist'])
                },
                error: err => {
                    console.info(err)
                    alert(err.error.error)
                }
            })
        });
    }

    createSignupForm(): FormGroup {
        return this.signupForm = this.fb.group({
            email: this.fb.control('', [Validators.required, Validators.email]),
            password: this.fb.control('', [Validators.required, Validators.minLength(3)])
        })
    }

    createLoginForm(): FormGroup {
        return this.loginForm = this.fb.group({
            email: this.fb.control('admin@admin.com', [Validators.required, Validators.minLength(4)]),
            password: this.fb.control('123', [Validators.required, Validators.minLength(3)])
        })
    }

    login() {
        const loginRequest: LoginRequest = {
            email: this.loginForm.value.email,
            password: this.loginForm.value.password,
        }
        this.loginService.login(loginRequest).subscribe({
            next: res => {
                this.storageService.saveUser(res)
                if (this.loginForm.value.email == res.email) {
                    this.router.navigate([res.email + '/productlist'])
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
            email: this.signupForm.value.email,
            password: this.signupForm.value.password
        }
        this.loginService.signup(signupRequest).subscribe({
            next: res => {
                console.info(res)
                this.storageService.saveUser(res)
                if (this.signupForm.value.email == res.email) {
                    this.router.navigate([res.email + '/profile'])
                } else {
                    throwError(() => new Error('Unable to login user: ' + res.email))
                }
            },
            error: err => {
                console.info(err)
                alert(err.error.error)
            }
        })
    }
}
