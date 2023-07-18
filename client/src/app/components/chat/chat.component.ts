import { AfterViewChecked, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { ChatMessage } from 'src/app/models/chatmessage.model';
import { Product } from 'src/app/models/product.model';
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

    @ViewChild('chat') chat!: ElementRef
    @ViewChildren('convoID') convoID!: QueryList<ElementRef>;

    messageSub$!: Subscription
    stompClient!: Stomp.Client
    messagesTo: string[] = []
    messagesFrom: string[] = []
    messages: ChatMessage[] = []
    messageInput = new FormControl('', [Validators.required, Validators.minLength(1)])
    email = this.storageService.getUser().email
    recipient!: string
    productID!: number
    chatID!: string
    profileImage!: string
    chatIDs: string[] = []
    convos: string[] = []
    product!: Product

    ngOnInit(): void {
        // this.productID = +this.activatedRoute.snapshot.params['productID']
        // this.recipient = this.activatedRoute.snapshot.params['seller']
        this.chatService.getAllConvos(this.email).subscribe(data => {
            let conversationsArray = this.groupBy(data, 'chatID')
            this.chatIDs = Object.keys(conversationsArray)
            for (var c of this.chatIDs) {
                let arr = c.split(',')
                arr = arr.filter(s => s != this.email)
                const convo = arr.join(' -> product ID: ')
                this.convos.push(convo)
            }
            console.info(this.convos)
        })

        this.productID = this.productService.productID
        this.recipient = this.productService.seller
        if (!!this.recipient && !!this.productID) {
            this.chatID = this.chatService.generateChatID(this.email, this.recipient, this.productID)
            this.getProductInfo(this.productID)
            this.chatIDs.push(this.chatID)
            this.chatService.getChatMessagesByID(this.chatID).subscribe(data => {
                this.messages = data
            })
        }

        this.profileService.getProfilePic(this.email).subscribe({
            next: data => {
                setTimeout(() => {
                    this.profileImage = data.url
                }, 300)
            }
        })

        const socket = new SockJS('http://localhost:8080/chat');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/topic/chat/' + this.chatID, data => {
                let message: ChatMessage = JSON.parse(data.body)
                this.messages.push(message)
                this.messages.sort((a, b) => {
                    if (a.timestamp! > b.timestamp!) {
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
        for (let i = 0; i < this.convoID.toArray().length; i++) {
            this.convoID.toArray()[i].nativeElement.innerText = this.convos[i]
        }
    }

    sendMessage(content: string) {
        const message: ChatMessage = {
            chatID: this.chatID,
            productID: this.productID,
            sender: this.email,
            recipient: this.recipient,
            content: content,
            timestamp: undefined
        }
        this.stompClient.send('/app/chat/' + this.chatID, {}, JSON.stringify({
            message
        }));
        this.messageInput.reset()
    }

    selectRecipient(value: any) {
        this.chatID = value
        this.chatService.getChatMessagesByID(this.chatID).subscribe(data => {
            this.messages = data
            this.productID = data[0].productID
            if (data[0].recipient != this.email) {
                this.recipient = data[0].recipient
            } else {
                this.recipient = data[0].sender
            }
            this.getProductInfo(this.productID)
        })
        
        const socket = new SockJS('http://localhost:8080/chat');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, (frame) => {
            this.stompClient.subscribe('/topic/chat/' + this.chatID, data => {
                let message: ChatMessage = JSON.parse(data.body)
                this.messages.push(message)
                this.messages.sort((a, b) => {
                    if (a.timestamp! > b.timestamp!) {
                        return 1
                    }
                    return -1
                })
            })
        })
    }

    getProductInfo(productID: number) {
        this.productService.getProduct(productID).subscribe(data => {
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
            }, {}  // initial value of objectToBeBuild
        );
    }
}
