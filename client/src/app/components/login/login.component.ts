import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

    fb = inject(FormBuilder)
    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)

    signupForm!: FormGroup
    loginForm!: FormGroup

}
