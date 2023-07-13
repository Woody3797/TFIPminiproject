import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { ProductService } from 'src/app/service/product.service';
import { StorageService } from 'src/app/service/storage.service';
import * as Stomp from 'stompjs';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy {

    storageService = inject(StorageService)
    productService = inject(ProductService)
    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)

    messageSub$!: Subscription
    stompClient!: Stomp.Client
    messages: string[] = []
    messageInput = new FormControl('', [Validators.required, Validators.minLength(1)])
    email = this.storageService.getUser().email
    recipient = 'b@b.com'
    productID!: number

    ngOnInit(): void {
        this.productID = +this.activatedRoute.snapshot.params['productID']
        const socket = new SockJS('http://localhost:8080/chat');
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

    sendMessage(content: string) {
        this.messages.push(content)
        this.stompClient.send('/app/' + this.recipient + '/chat/' + this.productID, {}, JSON.stringify({
            sender: this.email,
            content: content
        }));
        this.messageInput.reset()
    }

    selectRecipient() {
        this.recipient = ''
    }

}
