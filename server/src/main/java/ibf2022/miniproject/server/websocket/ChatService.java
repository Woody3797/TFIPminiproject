package ibf2022.miniproject.server.websocket;

import ibf2022.miniproject.server.model.User;
import ibf2022.miniproject.server.repository.UserRepository;
import ibf2022.miniproject.server.websocket.model.Chat;
import ibf2022.miniproject.server.websocket.model.ChatMessage;
import ibf2022.miniproject.server.websocket.repository.ChatMessageRepository;
import ibf2022.miniproject.server.websocket.repository.ChatRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ChatService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ChatRepository chatRepository;

  @Autowired
  private ChatMessageRepository chatMessageRepository;

  public List<Chat> getListChatRoomOfUser(String username) {
    return chatRepository.findByUsername("test");
  }

  public List<ChatMessage> getChatRoomMessage(Integer roomId) {
    return chatMessageRepository.getChatRoomMessage(roomId);
  }

  public void saveMessage(ChatMessage message) {
    System.out.println(message);
    Chat chatRoom = chatRepository.findById(1).get();

    ChatMessage chat = new ChatMessage();
    chat.setSenderName(message.getSenderName());
    chat.setReceiverName(message.getReceiverName());
    chat.setMessage(message.getMessage());
    chat.setTime(message.getTime());
    chat.setStatus(message.getStatus());
    chat.setChat(chatRoom);

    chatMessageRepository.save(chat);

  }

  public void createChatRoom(String sender, String receiver) {
    //check if both user are existed
    //check if there is a conversation already in database between 2 users
    Chat chat = new Chat();
    chat.setSenderName("test");
    chat.setReceiverName("test2");

    chatRepository.save(chat);
  }
}
