import { AfterViewChecked, AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, QueryList, Renderer2, ViewChild, ViewChildren, inject } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription, lastValueFrom } from 'rxjs';
import * as SockJS from 'sockjs-client';
import { ChatConvo, ChatMessage } from 'src/app/models/chatmessage.model';
import { Product } from 'src/app/models/product.model';
import { ChatService } from 'src/app/service/chat.service';
import { ProductService } from 'src/app/service/product.service';
import { ProfileService } from 'src/app/service/profile.service';
import { StorageService } from 'src/app/service/storage.service';
import * as Stomp from 'stompjs';

const SOCKET_KEY = 'http://localhost:8080/chat'

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked, AfterViewInit {

    router = inject(Router)
    activatedRoute = inject(ActivatedRoute)
    storageService = inject(StorageService)
    productService = inject(ProductService)
    chatService = inject(ChatService)
    profileService = inject(ProfileService)
    changeDetector = inject(ChangeDetectorRef)
    renderer = inject(Renderer2)

    @ViewChild('chat') chat!: ElementRef
    @ViewChildren('convoID') convoID!: QueryList<ElementRef>

    stompClient!: Stomp.Client
    convos$!: Subscription
    messages: ChatMessage[] = []
    messageInput = new FormControl('', [Validators.required, Validators.minLength(1)])
    email = this.storageService.getUser().email
    recipient!: string
    productID!: number
    chatID!: string
    profileImage = ''
    chatIDs: string[] = []
    product!: Product
    product$!: Subscription
    conversationsArray: any[] = []
    selectedIndex!: number

    ngOnInit(): void {
        const socket = new SockJS(SOCKET_KEY);
        this.stompClient = Stomp.over(socket);
        this.productID = this.productService.productID
        this.recipient = this.productService.seller

        this.convos$ = this.chatService.getAllConvos(this.email).subscribe(data => {
            let conversationsMap = this.groupBy(data, 'chatID')
            this.conversationsArray = Object.entries(conversationsMap).map(([chatID, messages]) => ({ chatID, messages }))
            this.chatIDs = Object.keys(conversationsMap)
            this.conversationsArray = this.conversationsArray.sort((a, b) => {
                if (a.messages[a.messages.length-1].timestamp > b.messages[b.messages.length-1].timestamp) {
                    return -1
                }
                return 1
            })
            for (let i = 0; i < this.conversationsArray.length; i++) {
                const c = this.conversationsArray[i].chatID
                let arr = c.split(',')
                arr = arr.filter((s: string) => s != this.email)
                const prodID = +arr[1]
                this.product$ = this.productService.getProduct(prodID).subscribe(d => {
                    this.conversationsArray[i]['product'] = d
                })
            }
            console.info(this.conversationsArray)
            if (!!this.recipient && this.productID > 0) {
                this.chatID = this.chatService.generateChatID(this.email, this.recipient, this.productID)
                this.getProductInfo(this.productID)
                this.chatIDs.push(this.chatID)
                this.chatService.getChatMessagesByID(this.chatID).subscribe(data => {
                    this.messages = data
                })
            }
            for (let i = 0; i < this.conversationsArray.length; i++) {
                if (this.chatID == this.conversationsArray[i].chatID) {
                    this.selectedIndex = i
                }
            }
        })

        this.profileService.getProfilePic(this.recipient).subscribe({
            next: data => {
                setTimeout(() => {
                    this.profileImage = data.url
                }, 300)
            },
            error: err => {console.info(err)}
        })

        this.stompClient.connect({}, (frame) => {
            for (var id of this.chatIDs) {
                this.stompClient.subscribe('/topic/chat/' + id, data => {
                    console.info(data)
                    let message: ChatMessage = JSON.parse(data.body)
                    if (message.chatID == this.chatID) {
                        this.messages.push(message)
                    }
                    this.messages.sort((a, b) => {
                        if (a.timestamp! > b.timestamp!) {
                            return 1
                        }
                        return -1
                    })
                    // this.sortConvo()
                })
            }
        })
    }

    ngOnDestroy(): void {
        this.convos$.unsubscribe()
        this.product$.unsubscribe()
        this.stompClient.disconnect(() => { console.info('disconnected') })
    }

    ngAfterViewChecked(): void {
        this.changeDetector.detectChanges()
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            for (let i = 0; i < this.convoID.toArray().length; i++) {
                const c = this.convoID.toArray()[i].nativeElement.innerText
                let arr = c.split(',')
                arr = arr.filter((s: string) => s != this.email)
                const prodID = +arr[1]
                const otherEmail = arr[0]
                this.product$ = this.productService.getProduct(prodID).subscribe(d => {
                    var displayConvo = `${otherEmail} -> Product Name: ${d.productName} (${prodID})`
                    // this.renderer.setProperty(this.convoID.toArray()[i].nativeElement, 'innerText', displayConvo)
                    this.convoID.toArray()[i].nativeElement.textContent = displayConvo
                })
            }
        }, 200);
    }

    sendMessage(content: string) {
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
        this.sortConvo()
        this.messageInput.reset()
        this.selectedIndex = 0
    }

    selectRecipient(chatID: any, i: number) {
        this.chatID = chatID
        this.selectedIndex = i
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
                    }, 300)
                },
                error: err => {this.profileImage = ''}
            })
        })
    }

    getProductInfo(productID: number) {
        this.productService.getProduct(productID).subscribe(data => {
            this.product = data
        })
    }

    sortConvo() {
        this.convos$ = this.chatService.getAllConvos(this.email).subscribe(data => {
            let conversationsMap = this.groupBy(data, 'chatID')
            this.conversationsArray = Object.entries(conversationsMap).map(([chatID, messages]) => ({ chatID, messages }))
            this.chatIDs = Object.keys(conversationsMap)
            this.conversationsArray = this.conversationsArray.sort((a, b) => {
                if (a.messages[a.messages.length-1].timestamp > b.messages[b.messages.length-1].timestamp) {
                    return -1
                }
                return 1
            })
            for (let i = 0; i < this.conversationsArray.length; i++) {
                const c = this.conversationsArray[i].chatID
                let arr = c.split(',')
                arr = arr.filter((s: string) => s != this.email)
                const prodID = +arr[1]
                const otherEmail = arr[0]
                this.product$ = this.productService.getProduct(prodID).subscribe(d => {
                    this.conversationsArray[i]['product'] = d
                    var displayConvo = `${otherEmail} -> Product Name: ${d.productName} (${prodID})`
                    this.convoID.toArray()[i].nativeElement.textContent = displayConvo
                })
            }
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
