import { Component, OnInit, inject } from '@angular/core';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

    storageService = inject(StorageService)


    email = ''


    ngOnInit(): void {
        this.email = this.storageService.getUser().email
    }

}
