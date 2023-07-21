import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/service/login.service';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

    router = inject(Router)
    loginService = inject(LoginService)
    storageService = inject(StorageService)


    email = ''


    ngOnInit(): void {
        this.email = this.storageService.getUser().email
    }

    gotoAddProduct() {
        this.router.navigate([ this.email + '/addproduct'])
    }

}
