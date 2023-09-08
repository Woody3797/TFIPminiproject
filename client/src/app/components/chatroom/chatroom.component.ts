import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, QueryList, Renderer2, ViewChild, ViewChildren, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { ChatMessage, ChatConvo } from 'src/app/models/chatmessage.model';
import { Product } from 'src/app/models/product.model';
import { ChatService } from 'src/app/service/chat.service';
import { ProductService } from 'src/app/service/product.service';
import { ProfileService } from 'src/app/service/profile.service';
import { StorageService } from 'src/app/service/storage.service';
import * as Stomp from 'stompjs';

const SOCKET_KEY = 'http://localhost:8080/chat'

@Component({
  selector: 'app-chatroom',
  templateUrl: './chatroom.component.html',
  styleUrls: ['./chatroom.component.css']
})
export class ChatroomComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {

    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)
    storageService = inject(StorageService)
    productService = inject(ProductService)
    chatService = inject(ChatService)
    profileService = inject(ProfileService)
    changeDetector = inject(ChangeDetectorRef)
    renderer = inject(Renderer2)


    stompClient!: Stomp.Client
    convos$!: Subscription
    product$!: Subscription
    profilePic$!: Subscription
    messages: ChatMessage[] = []
    messageInput = new FormControl('', [Validators.required, Validators.minLength(1)])
    email = this.storageService.getUser().email
    recipient!: string
    productID!: number
    chatID!: string
    profileImage = ''
    chatIDs: string[] = []
    product!: Product
    conversations: ChatConvo[] = []
    selectedIndex!: number

    ngOnInit(): void {
        const socket = new SockJS(SOCKET_KEY);
        this.stompClient = Stomp.over(socket);
        this.productID = this.productService.productID
        this.recipient = this.productService.seller

        this.convos$ = this.chatService.getAllConvos2(this.email).subscribe(data => {
            this.conversations = data
            console.info(this.conversations)
            for (var c of this.conversations) {
                this.chatIDs.push(c.chatID)
            }
        })

        this.profilePic$ = this.profileService.getProfilePic(this.recipient).subscribe({
            next: data => {
                setTimeout(() => {
                    this.profileImage = data.url
                }, 200)
            },
            error: err => {console.info(err)}
        })

        this.stompClient.connect({}, (frame) => {
            for (var id of this.chatIDs) {
                this.stompClient.subscribe('/topic/chat/' + id, data => {
                    
                })
            }
        })
    }

    ngOnDestroy(): void {
        if (this.product$) {
            this.product$.unsubscribe()
        }
        this.convos$.unsubscribe()
        this.profilePic$.unsubscribe()
        this.stompClient.disconnect(() => { console.info('disconnected') })
    }

    ngAfterViewChecked(): void {
        this.changeDetector.detectChanges()
    }

    ngAfterViewInit(): void {
    }

    sendMessage(content: string) {
        if (content.length == 0) {
            return;
        }
        const message: ChatMessage = {
            chatID: this.chatID,
            productID: this.productID,
            sender: this.email,
            recipient: this.recipient,
            content: content,
            timestamp: new Date
        }
        this.stompClient.send('/app/chat/' + this.chatID, {}, JSON.stringify({
            message
        }));
        this.messageInput.reset()
    }

    selectRecipient(chatID: any, i: number) {
        this.chatID = chatID
        this.selectedIndex = i
        // for (let i = 0; i < this.conversations.length; i++) {
        //     if (this.chatID == this.conversations[i].chatID) {
        //         this.conversations[i].newMessages = false
        //     }
        // }
        this.chatService.getChatMessagesByID(this.chatID).subscribe(data => {
            this.messages = data
            this.productID = data[0].productID
            if (data[0].recipient != this.email) {
                this.recipient = data[0].recipient
            } else {
                this.recipient = data[0].sender
            }
            this.getProductInfo(this.productID)
            this.profileService.getProfilePic(this.recipient).subscribe({
                next: data => {
                    this.profileImage = ''
                    setTimeout(() => {
                        this.profileImage = data.url
                    }, 200)
                },
                error: err => {this.profileImage = ''}
            })
        })
    }

    getProductInfo(productID: number) {
        this.product$ = this.productService.getProduct(productID).subscribe(data => {
            this.product = data
        })
    }

    private groupBy<T>(array: Array<T>, property: keyof T): { [key: string]: Array<T> } {
        return array.reduce(
            (objectToBeBuilt: { [key: string]: Array<T> }, arrayElem: T) => {
                const newOuterIdx = arrayElem[property] as unknown as string;
                if (!objectToBeBuilt[newOuterIdx]) {
                    objectToBeBuilt[newOuterIdx] = [];
                }
                if (arrayElem[property]) {
                    delete arrayElem[property];
                }
                objectToBeBuilt[newOuterIdx]?.push(arrayElem);
                return objectToBeBuilt;
            }, {});
    }


}