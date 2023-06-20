import { Component, OnInit, inject } from '@angular/core';
import { StorageService } from 'src/app/service/storage.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

    storageService = inject(StorageService)


    username = ''


    ngOnInit(): void {
        this.username = this.storageService.getUser().username
    }

}
