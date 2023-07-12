import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { StorageService } from 'src/app/service/storage.service';
import * as Stomp from 'stompjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

    storageService = inject(StorageService)
    router = inject(Router)

    messageSub$!: Subscription
    stompClient!: Stomp.Client
    messages: string[] = []
    messageInput = new FormControl('', [Validators.required, Validators.minLength(1)])
    email = this.storageService.getUser().email
    sendTo = 'b@b.com'

    ngOnInit(): void {
        const socket = new SockJS('http://localhost:8080/chat', {
            
        });
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/topic/chat', data => {
                console.info(data)
            })
        })
    }

    ngOnDestroy(): void {
        this.stompClient.disconnect(() => {console.info('disconnected')})
    }

    sendMessage(value: string) {
        this.messages.push(value)
        this.stompClient.send('/app/chat/' + this.sendTo, {}, JSON.stringify({
            sender: this.email,
            content: value
        }));
        this.messageInput.reset()
    }

    selectRecipient() {
        this.sendTo = ''
    }

}
