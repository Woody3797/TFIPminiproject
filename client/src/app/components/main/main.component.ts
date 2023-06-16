import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

    router = inject(Router)
    loginService = inject(LoginService)


    username = ''


    ngOnInit(): void {
        this.username = this.loginService.username
        
    }

}
