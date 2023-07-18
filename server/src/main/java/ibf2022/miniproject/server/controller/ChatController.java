package ibf2022.miniproject.server.controller;

import java.io.StringReader;
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
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Controller
@RequestMapping(path = "/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;
    
    @MessageMapping("/chat/{chatID}")
    @SendTo("/topic/chat/{chatID}")
    public ChatMessage addMessage(String payload, @DestinationVariable("chatID") String chatID) {
        JsonObject jo = Json.createReader(new StringReader(payload)).readObject();
        ChatMessage message = new ChatMessage();
        message.setChatID(chatID);
        message.setProductID(jo.getJsonObject("message").getInt("productID"));
        message.setSender(jo.getJsonObject("message").getString("sender"));
        message.setRecipient(jo.getJsonObject("message").getString("recipient"));
        message.setContent(jo.getJsonObject("message").getString("content"));
        System.out.println(message);
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
