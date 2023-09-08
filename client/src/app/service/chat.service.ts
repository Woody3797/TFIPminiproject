import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ChatConvo, ChatMessage } from '../models/chatmessage.model';

@Injectable({
    providedIn: 'root'
})
export class ChatService {

    http = inject(HttpClient)

    getChatMessagesByID(chatID: string): Observable<ChatMessage[]> {
        const params = new HttpParams().set('chatID', chatID)

        return this.http.get<ChatMessage[]>('/api/chat/getmessages', { params })
    }

    getAllConvos(email: string): Observable<ChatMessage[]> {
        const params = new HttpParams().set('email', email)

        return this.http.get<ChatMessage[]>('/api/chat/getallconvos', { params })
    }

    getAllConvos2(email: string): Observable<ChatConvo[]> {
        const params = new HttpParams().set('email', email)

        return this.http.get<ChatConvo[]>('/api/chat/getallconvos2', { params })
    }


    generateChatID(chatter1: string, chatter2: string, productID: number) {
        let chatters: string = ''

        if (chatter1 < chatter2) {
            chatters = chatter1 + ',' + chatter2
        } else {
            chatters = chatter2 + ',' + chatter1
        }
        return chatters + ',' + productID
    }
}
