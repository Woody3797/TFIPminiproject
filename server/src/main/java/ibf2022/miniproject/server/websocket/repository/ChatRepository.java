package ibf2022.miniproject.server.websocket.repository;

import ibf2022.miniproject.server.websocket.model.Chat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Integer> {

  @Query("Select c from Chat c where c.senderName = ?1 or c.receiverName= ?1")
  List<Chat> findByUsername(String username);
}
