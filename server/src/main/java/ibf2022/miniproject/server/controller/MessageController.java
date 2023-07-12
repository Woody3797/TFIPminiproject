package ibf2022.miniproject.server.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.miniproject.server.model.ChatMessage;

@Controller
@RequestMapping()
public class MessageController {
    
    @MessageMapping("/chat/{recipient}")
    @SendTo("/topic/chat")
    public String addMessage(ChatMessage message, @DestinationVariable("recipient") String recipient) {
        System.out.println(recipient);
        System.out.println(message.toString());

        return "chat message received, sending ";
    }
}
