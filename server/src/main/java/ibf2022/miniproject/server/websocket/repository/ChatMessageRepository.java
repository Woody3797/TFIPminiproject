package ibf2022.miniproject.server.websocket.repository;

import ibf2022.miniproject.server.websocket.model.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,Integer> {

  @Query("select cm from ChatMessage cm where cm.chat.id =?1 order by cm.time")
  List<ChatMessage> getChatRoomMessage(Integer roomId);
}
