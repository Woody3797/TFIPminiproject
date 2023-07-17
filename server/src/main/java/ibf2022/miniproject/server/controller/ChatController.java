package ibf2022.miniproject.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ibf2022.miniproject.server.model.ChatMessage;
import ibf2022.miniproject.server.service.ChatService;

@Controller
@RequestMapping(path = "/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    
    @MessageMapping("{recipient}/chat/{productID}")
    @SendTo("/topic/chat/{recipient}")
    public ChatMessage addMessage(ChatMessage message, @DestinationVariable("recipient") String recipient, @DestinationVariable("productID") Integer productID) {
        ChatMessage result = chatService.saveChatMessage(message);

        return result;
    }

    @GetMapping(path = "/getmessages")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getChatMessagesByID(@RequestParam String chatID) {
        List<ChatMessage> messages = chatService.getChatMessagesByID(chatID);
        return ResponseEntity.ok(messages);
    }

    @GetMapping(path = "/getallconvos")
    @ResponseBody
    public ResponseEntity<List<ChatMessage>> getAllConvos(@RequestParam String sender) {
        List<ChatMessage> messages = chatService.getAllConvos(sender);
        return ResponseEntity.ok(messages);
    }



}
