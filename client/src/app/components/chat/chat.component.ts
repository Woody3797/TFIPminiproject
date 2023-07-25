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

// const SOCKET_KEY = 'http://localhost:8080/chat'
const SOCKET_KEY = 'https://fluttering-sock-production.up.railway.app/chat'

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
                const prodID = +arr[2]
                arr = arr.filter((s: string) => s != this.email)
                this.product$ = this.productService.getProduct(prodID).subscribe(d => {
                    this.conversationsArray[i]['product'] = d
                    this.conversationsArray[i]['newMessages'] = false
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
                }, 200)
            },
            error: err => {console.info(err)}
        })

        this.stompClient.connect({}, (frame) => {
            for (var id of this.chatIDs) {
                this.stompClient.subscribe('/topic/chat/' + id, data => {
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
                    this.convos$ = this.chatService.getAllConvos(this.email).subscribe(data => {
                        setTimeout(() => {
                            this.sortConvo(data)
                        }, 100);
                        for (let i = 0; i < this.conversationsArray.length; i++) {
                            if (this.chatID == this.conversationsArray[i].chatID) {
                                this.selectedIndex = i
                            }
                            if (message.chatID != this.chatID && message.chatID == this.conversationsArray[i].chatID) {
                                this.conversationsArray[i]['newMessages'] = true
                            }
                        }
                    })
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
            this.sortConvo(this.conversationsArray)
            // for (let i = 0; i < this.convoID.toArray().length; i++) {
            //     const c = this.convoID.toArray()[i].nativeElement.innerText.trim()
            //     let arr = c.split(',')
            //     const prodID = +arr[2]
            //     arr = arr.filter((s: string) => s != this.email)
            //     const otherEmail = arr[0]
            //     this.product$ = this.productService.getProduct(prodID).subscribe(d => {
            //         var displayConvo = `${otherEmail} - Product ID: (${prodID})`
            //         this.convoID.toArray()[i].nativeElement.textContent = displayConvo
            //         // this.renderer.setProperty(this.convoID.toArray()[i].nativeElement, 'textContent', displayConvo)
            //     })
            // }
        }, 150);
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
        for (let i = 0; i < this.conversationsArray.length; i++) {
            if (this.chatID == this.conversationsArray[i].chatID) {
                this.conversationsArray[i].newMessages = false
            }
        }
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
            }, {});
    }

    sortConvo(data: ChatMessage[]) {
        let conversationsMap = this.groupBy(data, 'chatID')
        let conversationsArray2: any[] = Object.entries(conversationsMap).map(([chatID, messages]) => ({ chatID, messages }))
        this.chatIDs = Object.keys(conversationsMap)
        conversationsArray2 = conversationsArray2.sort((a, b) => {
            if (a.messages[a.messages.length-1].timestamp > b.messages[b.messages.length-1].timestamp) {
                return -1
            }
            return 1
        })
        for (let i = 0; i < conversationsArray2.length; i++) {
            if (this.conversationsArray[i].newMessages) {
                let newMessageID = this.conversationsArray[i].chatID
                for (let j = 0; j < conversationsArray2.length; j++) {
                    if (conversationsArray2[j].chatID == newMessageID) {
                        conversationsArray2[j]['newMessages'] = true
                        console.info(conversationsArray2)
                    }
                }
            }
            const c = conversationsArray2[i].chatID.trim()
            let arr = c.split(',')
            const prodID = +arr[2]
            arr = arr.filter((s: string) => s != this.email)
            const otherEmail = arr[0]
            this.product$ = this.productService.getProduct(prodID).subscribe(d => {
                conversationsArray2[i]['product'] = d
                var displayConvo = `${otherEmail} - Product ID: (${prodID})`
                this.renderer.setProperty(this.convoID.toArray()[i].nativeElement, 'textContent', displayConvo)
            })
            if (conversationsArray2[i].chatID == this.chatID) {
                this.selectedIndex = i
            }
        }
        this.conversationsArray = conversationsArray2
    }

}
