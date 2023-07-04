package ibf2022.miniproject.server.websocket;

import ibf2022.miniproject.server.ApiResponse;
import ibf2022.miniproject.server.websocket.model.Chat;
import ibf2022.miniproject.server.websocket.model.ChatMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  @Autowired
  private ChatService chatService;

  @GetMapping("/api/chat-room")
  public ResponseEntity<List<Chat>> getChatRoom(@RequestParam String username){
    return new ResponseEntity<>(chatService.getListChatRoomOfUser(username),HttpStatus.OK);
  }

  @GetMapping("/api/chat-room/{id}")
  public ResponseEntity<List<ChatMessage>> getChatRoom(@PathVariable(name = "id") Integer id){
    return new ResponseEntity<>(chatService.getChatRoomMessage(id),HttpStatus.OK);
  }

  @GetMapping("/api/chat-room/create-new-chat-room")
  public ResponseEntity<ApiResponse> createChatRoom(@RequestParam String sender, @RequestParam String receiver){
    chatService.createChatRoom(sender,receiver);
    return new ResponseEntity<>(new ApiResponse(true,"Create successfully!"),HttpStatus.CREATED);
  }
  @MessageMapping("/message")
  @SendTo("/chatroom/hello")
  public ChatMessage hello(@Payload ChatMessage message) {
    return message;
  }
  @MessageMapping("/private-message")
  public ChatMessage chatting(@Payload ChatMessage message) {
    simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
    chatService.saveMessage(message);
    return message;
  }
}
