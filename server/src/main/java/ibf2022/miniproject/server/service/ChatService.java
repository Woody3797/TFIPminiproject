package ibf2022.miniproject.server.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import ibf2022.miniproject.server.model.ChatConvo;
import ibf2022.miniproject.server.model.ChatMessage;
import ibf2022.miniproject.server.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;


    public Integer saveChatConvo(ChatMessage message, String recipient, Integer productID) {
        message.setTimestamp(LocalDateTime.now());
        String sender = message.getSender();
        String chatters = combineChatters(sender, recipient);

        System.out.println(chatters);

        try {
            ChatConvo convo1 = chatRepository.getChatConvo(message.getSender(), recipient, productID);
            if (convo1 != null) {
                System.out.println("dupe");
                return null;
            }
            ChatConvo convo2 = chatRepository.getChatConvo(recipient, message.getSender(), productID);
            if (convo2 != null) {
                System.out.println("dupe");
                return null;
            }
            Integer chatID = chatRepository.saveChatConvo(message.getSender(), recipient, productID);
            System.out.println(chatID);
            return chatID;
        } catch (EmptyResultDataAccessException e) {
            Integer chatID = chatRepository.saveChatConvo(message.getSender(), recipient, productID);
            System.out.println(chatID);
            return chatID;
        }
    }


    public String combineChatters(String sender, String recipient) {
        int compare = sender.compareToIgnoreCase(recipient);
        String chatters = "";
        if (compare < 0) {
            chatters = sender.concat(",").concat(recipient);
        } else if (compare > 0) {
            chatters = recipient.concat(",").concat(sender);
        } else {
            return null;
        }
        return chatters;
    }
    
}
