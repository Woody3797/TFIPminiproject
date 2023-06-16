import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginRequest, SignupRequest } from 'src/app/models/login.model';
import { LoginService } from 'src/app/service/login.service';

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

    signupForm!: FormGroup
    loginForm!: FormGroup


    ngOnInit(): void {
        this.signupForm = this.createSignupForm()
        this.loginForm = this.createLoginForm()
        
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
            username: this.fb.control('', [Validators.required, Validators.minLength(4)]),
            password: this.fb.control('', [Validators.required, Validators.minLength(3)])
        })
    }

    login() {
        const loginRequest: LoginRequest = {
            username: this.loginForm.value.username,
            password: this.loginForm.value.password,
        }
        this.loginService.login(loginRequest).subscribe(data => {
            console.info(data)
        })
    }

    signup() {
        const signupRequest: SignupRequest = {
            username: this.signupForm.value.username,
            email: this.signupForm.value.email,
            password: this.signupForm.value.password
        }
        this.loginService.signup(signupRequest).subscribe(data => {
            console.info(data)
        })
    }
}
