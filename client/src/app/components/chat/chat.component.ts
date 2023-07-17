import { AfterViewChecked, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { ChatMessage } from 'src/app/models/chatmessage.model';
import { ChatService } from 'src/app/service/chat.service';
import { ProductService } from 'src/app/service/product.service';
import { ProfileService } from 'src/app/service/profile.service';
import { StorageService } from 'src/app/service/storage.service';
import * as Stomp from 'stompjs';

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {

    storageService = inject(StorageService)
    productService = inject(ProductService)
    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)
    chatService = inject(ChatService)
    profileService = inject(ProfileService)
    changeDetector = inject(ChangeDetectorRef)

    @ViewChild('chat')
    chat!: ElementRef

    messageSub$!: Subscription
    stompClient!: Stomp.Client
    messagesTo: string[] = []
    messagesFrom: string[] = []
    messages: ChatMessage[] = []
    chatMessages: ChatMessage[] = []
    messageInput = new FormControl('', [Validators.required, Validators.minLength(1)])
    email = this.storageService.getUser().email
    recipient!: string
    productID!: number
    chatID!: string
    conversations: string[] = []
    profileImage!: string

    ngOnInit(): void {
        this.productID = +this.activatedRoute.snapshot.params['productID']
        this.recipient = this.activatedRoute.snapshot.params['seller']
        this.chatID = this.chatService.generateChatID(this.email, this.recipient, this.productID)
        console.info(this.chatID)

        this.chatService.getChatMessagesByID(this.chatID).subscribe(data => {
            // this.messages = data
        })

        this.profileService.getProfilePic(this.email).subscribe({
            next: data => {
                this.profileImage = ''
                setTimeout(() => {
                    console.info('sleeping')
                    this.profileImage = data.url
                }, 1111)
            }
        })

        this.chatService.getAllConvos(this.email).subscribe(data => {
            this.messages = data
            console.info(data)
            let array: string[] = []
            for (var val of data) {
                array = array.concat(val.sender).concat(val.recipient)
            }
            let uniqueChatters = [...new Set(array)]
            this.conversations = uniqueChatters.filter(item => item != this.email)
            console.info(this.conversations)
            
            this.profileService.getProfilePic(this.email).subscribe({
                next: data => {
                    this.profileImage = ''
                    setTimeout(() => {
                        console.info('sleeping')
                        this.profileImage = data.url
                    }, 1111)
                }
            })
        })


        const socket = new SockJS('http://localhost:8080/chat');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/topic/chat/' + this.recipient, data => {
                console.info('data from sendto ', data)
                let message: ChatMessage = JSON.parse(data.body)
                console.info(message)
                this.messages.push(message)
                this.messages.sort((a, b) => {
                    if (a.timestamp > b.timestamp) {
                        return 1
                    }
                    return -1
                })
            })
        })
    }

    ngOnDestroy(): void {
        this.stompClient.disconnect(() => { console.info('disconnected') })
    }

    ngAfterViewChecked(): void {
        this.changeDetector.detectChanges()
    }

    sendMessage(content: string) {
        // this.messages.push(content)
        this.stompClient.send('/app/' + this.recipient + '/chat/' + this.productID, {}, JSON.stringify({
            chatID: this.chatID,
            productID: this.productID,
            sender: this.email,
            recipient: this.recipient,
            content: content,
        }));
        this.messageInput.reset()
    }

    selectRecipient() {
        console.info(this.recipient)
    }

    formatTimeStamp(timeStamp: string) {
        const endDate = timeStamp.indexOf('-');
        return (
          timeStamp.substring(0, endDate) + ' ' + timeStamp.substring(endDate + 1)
        );
      }


}
