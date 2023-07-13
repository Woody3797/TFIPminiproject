package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.miniproject.server.model.ChatMessage;
import ibf2022.miniproject.server.service.ChatService;

@Controller
@RequestMapping()
public class MessageController {

    @Autowired
    private ChatService chatService;
    
    @MessageMapping("{recipient}/chat/{productID}")
    @SendTo("/topic/chat")
    public String addMessage(ChatMessage message, @DestinationVariable("recipient") String recipient, @DestinationVariable("productID") Integer productID) {
        System.out.println(productID);
        System.out.println(message.toString());
        Integer chatID = chatService.saveChatConvo(message, recipient, productID);
        System.out.println(chatID);

        return "chat id is " + chatID;
    }
}
