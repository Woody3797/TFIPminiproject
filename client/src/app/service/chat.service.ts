import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ChatMessage } from '../models/chatmessage.model';

@Injectable({
    providedIn: 'root'
})
export class ChatService {

    http = inject(HttpClient)

    getChatMessagesByID(chatID: string): Observable<ChatMessage[]> {
        const params = new HttpParams().set('chatID', chatID)

        return this.http.get<ChatMessage[]>('/api/chat/getmessages', { params })
    }

    getAllConvos(sender: string): Observable<ChatMessage[]> {
        const params = new HttpParams().set('sender', sender)

        return this.http.get<ChatMessage[]>('/api/chat/getallconvos', { params })
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
